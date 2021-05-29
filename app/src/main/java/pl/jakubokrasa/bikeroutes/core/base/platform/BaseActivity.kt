package pl.jakubokrasa.bikeroutes.core.base.platform

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<T: BaseViewModel>: AppCompatActivity() {
    abstract val viewModel: T

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        viewModel.message.observe(this, {
            showToast(it)
        })
    }

    protected fun showToast(it: String?) {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }
}