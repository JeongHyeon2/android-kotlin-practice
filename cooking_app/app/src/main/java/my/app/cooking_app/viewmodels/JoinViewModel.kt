package my.app.cooking_app.viewmodels

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import my.app.cooking_app.utils.App
import my.app.cooking_app.views.IntroActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinViewModel : ViewModel() {
    var email = MutableLiveData("")
    var pwd = MutableLiveData("")
    var pwdCheck = MutableLiveData("")
    private var auth: FirebaseAuth = Firebase.auth
    private val context = App.context()
    private val _loadingState = MutableLiveData<Boolean>(false)
    val loadingState: LiveData<Boolean> get() = _loadingState

    private fun checkValidator(): Boolean {

        if (email.value == "" || pwd.value == "" || pwdCheck.value == "") {
            Toast.makeText(context, "값을 모두 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.value!!.contains(" ") || pwd.value!!.contains(" ") || pwdCheck.value!!.contains(" ")) {
            Toast.makeText(context, "값에는 공백을 포함할 수 없습니다", Toast.LENGTH_SHORT).show()
            return false
        }
        if (pwd.value != pwdCheck.value) {
            Toast.makeText(context, "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!email.value!!.matches("^[A-Za-z0-9](.*)([@]{1})(.{1,})(\\.)(.{1,})".toRegex())) {
            Toast.makeText(context, "이메일 형식이 올바르지 않습니다", Toast.LENGTH_SHORT).show()
            return false
        }
        if (pwd.value!!.length < 6) {
            Toast.makeText(context, "비밀번호는 6자리 이상이어야 합니다", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun onClickJoinBtn() {
        try {
                _loadingState.value = true
            if (checkValidator()) {
                auth.createUserWithEmailAndPassword(email.value.toString(), pwd.value.toString())
                    .addOnCompleteListener { task ->
                        _loadingState.value = false
                        if (task.isSuccessful) {
                            sendEmailVerification()

                        } else {
                            if (task.exception is FirebaseAuthUserCollisionException) {
                                Toast.makeText(context, "이미 존재하는 이메일입니다", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
            }
        }catch (e:Exception){
            Log.d("JoinViewModel",e.toString())
        }
    }
    private fun sendEmailVerification() {
        try{
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 이메일 확인 이메일이 성공적으로 보내진 경우
                    Toast.makeText(context, "${email.value.toString()}로 \n인증메일을 보냈습니다", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, IntroActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)

                } else {
                    // 이메일 확인 이메일을 보내는 데 실패한 경우
                    val exception = task.exception
                    Toast.makeText(context, "이메일 확인 이메일을 보내는 데 실패했습니다", Toast.LENGTH_SHORT).show()
                    // 예외 처리를 수행할 수 있습니다.
                }
            }
        }catch (e:Exception){
            Log.d("JoinViewModel",e.toString())
        }
    }

}