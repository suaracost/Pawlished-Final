package com.example.pawlished

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.auth.FirebaseUser

class LoginClienteActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var correoEditText: EditText
    private lateinit var contraseñaEditText: EditText
    private  lateinit var iniciarSesionButton: Button
    private lateinit var registrarClienteButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_cliente)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("clientes")

         correoEditText = findViewById(R.id.emailEditText)
        contraseñaEditText = findViewById(R.id.passwordEditText)
         iniciarSesionButton  = findViewById(R.id.iniciarSesionButton)
         registrarClienteButton = findViewById(R.id.registrarClienteButton)

        iniciarSesionButton.setOnClickListener {
            val correo = correoEditText.text.toString()
            val contraseña = contraseñaEditText.text.toString()

            if (correo.isNotEmpty() && contraseña.isNotEmpty()) {
                auth.signInWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user: FirebaseUser? = auth.currentUser
                            if (user != null) {
                                val uid = user.uid
                                val userRef = databaseReference.child(uid)

                                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        val tipoUsuario =
                                            dataSnapshot.child("tipoUsuario").getValue(String::class.java)

                                        if (tipoUsuario == "Cliente") {
                                            // Inicio de sesión exitoso para cliente, ir a MainActivityCliente
                                            val intent =
                                                Intent(this@LoginClienteActivity, MainActivityCliente::class.java)
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            // Tipo de usuario incorrecto, mostrar mensaje y redirigir
                                            auth.signOut() // Cerrar la sesión
                                            Toast.makeText(
                                                this@LoginClienteActivity,
                                                "No tienes permiso para acceder a esta área.",

                                                Toast.LENGTH_SHORT
                                            ).show()
                                            val intent1 = Intent(this@LoginClienteActivity, LoginPeluqueriaActivity::class.java)
                                            startActivity(intent1)
                                            finish()
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Manejar errores de base de datos aquí
                                        Toast.makeText(
                                            this@LoginClienteActivity,
                                            "Error al acceder a la base de datos.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                            }
                        } else {
                            // El inicio de sesión falló, mostrar un mensaje de error
                            val exception = task.exception
                            if (exception != null) {
                                // Manejar errores de autenticación
                                Toast.makeText(
                                    this@LoginClienteActivity,
                                    "Inicio de sesión fallido. Verifica tus credenciales.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
            } else {
                Toast.makeText(
                    this@LoginClienteActivity,
                    "Por favor, completa todos los campos.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        registrarClienteButton.setOnClickListener {
            val intent = Intent(this, RegistroClienteActivity::class.java)
            startActivity(intent)
        }
    }
}
