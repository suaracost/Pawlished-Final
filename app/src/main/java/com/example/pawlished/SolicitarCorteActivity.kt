package com.example.pawlished

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast

class SolicitarCorteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitar_corte)

        val volverMainButton: Button = findViewById(R.id.volverMainButton)
        val solicitarButton: Button = findViewById(R.id.solicitarButton)
        val serviciosCheckboxList = listOf(
            findViewById<CheckBox>(R.id.bañoCheckBox),
            findViewById<CheckBox>(R.id.corteCheckBox),
            findViewById<CheckBox>(R.id.deslanadoCheckBox),
            findViewById<CheckBox>(R.id.bañoEspecialCheckBox),
            findViewById<CheckBox>(R.id.uñasCheckBox),
            findViewById<CheckBox>(R.id.cuidadoOidosCheckBox),
            findViewById<CheckBox>(R.id.accesoriosCheckBox),
            findViewById<CheckBox>(R.id.fraganciasCheckBox)
        )

        solicitarButton.setOnClickListener {
            val serviciosSeleccionados = obtenerServiciosSeleccionados(serviciosCheckboxList)

            if (serviciosSeleccionados.isNotEmpty()) {
                // Darve: implementar la solicitud de permiso y mostrar el mapa  mostrar, tambien  implementar la lógica para buscar y mostrar peluquerías cercanas
                val intent = Intent(this, ViewNearestActivity::class.java)
                intent.putStringArrayListExtra("servicios_seleccionados", serviciosSeleccionados)
                startActivity(intent)
            } else {
                Toast.makeText(this, "No has seleccionado ningún servicio", Toast.LENGTH_SHORT).show()
            }
        }

        volverMainButton.setOnClickListener {
            val intent = Intent(this, MainActivityCliente::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun obtenerServiciosSeleccionados(checkboxList: List<CheckBox>): ArrayList<String> {
        val serviciosSeleccionados = ArrayList<String>()
        for (checkBox in checkboxList) {
            if (checkBox.isChecked) {
                serviciosSeleccionados.add(checkBox.text.toString())
            }
        }
        return serviciosSeleccionados
    }
}
