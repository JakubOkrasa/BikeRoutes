package pl.jakubokrasa.bikeroutes.core.base.platform

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<T: BaseViewModel>: AppCompatActivity() {
    abstract val viewModel: T

    protected fun showToast(it: String?) {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }

    protected open fun onPendingState() {}

    protected open fun onIdleState() {}
}