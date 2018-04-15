package com.parkingreservation.iuh.demologinmvp.ui.login

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.parkingreservation.iuh.demologinmvp.R
import com.parkingreservation.iuh.demologinmvp.base.BaseActivity
import com.parkingreservation.iuh.demologinmvp.databinding.ActivityLoginBinding
import com.parkingreservation.iuh.demologinmvp.model.LoginModel

class LoginActivity : BaseActivity<LoginPresenter>(), LoginContract.View {

    @BindView(R.id.input_name)
    lateinit var inputUserName: EditText

    @BindView(R.id.input_password)
    lateinit var inputPassword: EditText

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        ButterKnife.bind(this)

        presenter.onViewCreated()
    }

    override fun updateUser(users: List<LoginModel>) {
//        inputUserName.setText("aaaa")
        binding.user = users[0]
    }

    override fun onLoginSuccessfully() {
        finish()
    }

    override fun getUserName(): String {
        return inputUserName.text.toString()
    }

    override fun getPassword(): String {
        return inputPassword.text.toString()
    }

    override fun showError(string: String) {
        showStatus(string)
    }

    override fun showSuccess(string: String) {
        showStatus(string)
    }

    override fun showLoading() {
        binding.progressVisibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.progressVisibility = View.GONE
    }

    private fun showStatus(s: String) {
        Toast.makeText(getContexts(), s, Toast.LENGTH_LONG).show()
    }


    override fun instantiatePresenter(): LoginPresenter {
        return LoginPresenter(this)
    }

    override fun getContexts(): Context { return this }

    @OnClick(R.id.btn_signIn)
    public fun signIn() {
        val userName = getUserName()
        val password = getPassword()
        presenter.signIn(userName, password)
    }
}
