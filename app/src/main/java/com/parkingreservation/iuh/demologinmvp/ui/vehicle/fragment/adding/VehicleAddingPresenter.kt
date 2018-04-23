package com.parkingreservation.iuh.demologinmvp.ui.vehicle.fragment.adding

import android.util.Log
import com.parkingreservation.iuh.demologinmvp.R
import com.parkingreservation.iuh.demologinmvp.base.BasePresenter
import com.parkingreservation.iuh.demologinmvp.model.User
import com.parkingreservation.iuh.demologinmvp.model.Vehicle
import com.parkingreservation.iuh.demologinmvp.model.VehicleTypes
import com.parkingreservation.iuh.demologinmvp.service.VehicleService
import com.parkingreservation.iuh.demologinmvp.ui.ticket.fragment.history.TicketHistoryPresenter
import com.parkingreservation.iuh.demologinmvp.util.MySharedPreference
import com.parkingreservation.iuh.demologinmvp.util.MySharedPreference.SharedPrefKey.Companion.USER
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class VehicleAddingPresenter(view: VehicleAddingContract.View): BasePresenter<VehicleAddingContract.View>(view), VehicleAddingContract.Presenter {
    companion object {
        var TAG = VehicleAddingPresenter::class.java.simpleName
    }

    private
    var subscription: Disposable? = null

    @Inject
    lateinit var pref: MySharedPreference

    @Inject
    lateinit var vehicleService: VehicleService

    override fun saveVehicle(vehicle: Vehicle) {
        view.showLoading()
        Log.i(TAG, "saving... vehicle of user")
        if(isLoggedIn()) {
            val id = getUserId()
            vehicle.driverID = id!!
            subscription = vehicleService.addVehicle(vehicle)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnTerminate { view.hideLoading() }
                    .subscribe(
                            {
                                Log.i(TAG, "add vehicle successfully")
                                view.showSuccess("add vehicle successfully")
                            },
                            {
                                view.showError("oOps!!, there is some error from server, pls try again")
                                Log.w(TAG, "There is some thing error while creating Vehicle ${it.message}")
                            }
                    )
        } else {
            view.showError("Hey!!, You are not logged in yet")
            Log.w(TicketHistoryPresenter.TAG, "User are not logged in yet")
        }
    }

    fun getListVehicleType(): MutableList<VehicleTypes> {
        val list = mutableListOf<VehicleTypes>()
        list.add(VehicleTypes("2", "Car", R.drawable.ic_vehicle_car))
        list.add(VehicleTypes("4", "Bus", R.drawable.ic_vehicle_bus))
        list.add(VehicleTypes("1", "Bike", R.drawable.ic_vehicle_bike))
        list.add(VehicleTypes("3", "Truck", R.drawable.ic_vehicle_truck))
        return list
    }

    private fun getUserId(): String? {
        return (pref.getData(USER, User::class.java) as User).userID!!
    }


    override fun onViewCreated() {
    }

    override fun onViewDestroyed() {
        subscription?.dispose()
    }

    private fun isLoggedIn(): Boolean = pref.getData(USER, User::class.java) != null
}