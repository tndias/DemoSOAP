package br.com.psoa.demosoap

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

class MainActivity : AppCompatActivity() {

    private val url = "http://10.3.2.42:8080/CalculadoraWSService/CalculadoraWS?wsdl"
    private val nameSpace = "http://heiderlopes.com.br/"
    private val methodName = "calcular"
    private val soapAction = nameSpace + methodName
    private val parm1 = "num1"
    private val parm2 = "num2"
    private val parm3 = "op"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btCalc.setOnClickListener(
            {
                CallWebService().execute(etNumber1.text.toString(),
                    etNumber2.text.toString(), spOperacoes.selectedItem.toString())
            }
        )

    }

    inner class CallWebService : AsyncTask<String, Void, String>() {

        override fun onPostExecute(result: String?) {
            tvResult.text = result
        }

        override fun doInBackground(vararg params: String?): String {
            var result = ""
            var soapObject = SoapObject(nameSpace, methodName)
            val n1 = PropertyInfo()
            n1.name = parm1
            n1.value = params[0]
            n1.type = Integer::class.java

            soapObject.addProperty(n1)

            val n2 = PropertyInfo()
            n2.name = parm2
            n2.value = params[1]
            n2.type = Integer::class.java

            soapObject.addProperty(n2)

            val n3 = PropertyInfo()
            n3.name = parm3
            n3.value = params[2]
            n3.type = String::class.java

            soapObject.addProperty(n3)

            val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
            envelope.setOutputSoapObject(soapObject)

            val httpTransportSE = HttpTransportSE(url)

            try {
                httpTransportSE.call(soapAction, envelope)
                val soapPrimitive = envelope.response
                result = soapPrimitive.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }

    }
}
