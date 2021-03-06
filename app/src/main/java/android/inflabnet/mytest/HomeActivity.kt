package android.inflabnet.mytest

import android.content.Intent
import android.inflabnet.mytest.login.LoginActivity
import android.inflabnet.mytest.mesas.model.UserTokens
import android.inflabnet.mytest.mesas.model.fechouConta
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_conta_chat.*

class HomeActivity : AppCompatActivity() {

    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //instanciando o banco
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference
        token = intent.getStringExtra("token")
        upToken()
    }

    fun upToken(){
        var userToken = pegarUser()
        if (userToken != null) {
            userToken = userToken.replace(".","")
        }
        //val token = pegaToken()
        //atiualizar firebase com o nome do user e token
        //referencia do caminho
        val dbRefe = mDatabaseReference!!
        //gerar a key
        val tkTimestamp = System.currentTimeMillis().toString()
        //montar o objeto
        val userTk = userToken?.let { token?.let { it1 -> UserTokens(it, it1,tkTimestamp) } }
        if (userToken != null) {
            dbRefe.child("UserTokens").child(userToken).setValue(userTk)
        }
    }

    //não precisa falar. Ok pega o usuário que está logado
    private fun pegarUser(): String? {
        //pegar o usuário
        val userEmail = mAuth?.currentUser?.email
        var user: String? = null
        //val user: String
        if (userEmail != null) {
            if (userEmail.contains("@")) {
                user =
                        userEmail.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            } else {
                user = userEmail
            }
        }else {
            val intt = Intent(this, LoginActivity::class.java)
            startActivity(intt)
        }
        return user
    }

}
