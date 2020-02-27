package com.allen.getsunset

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.CheckedInputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    protected fun GetSunset(view: View) {
        var city = etCityName.text.toString();
        val url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+ city +"%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"
        MyAsyncTask().execute(url)
    }

    //To Get some data from api
    inner class MyAsyncTask: AsyncTask<String, String, String>() {
        override fun onPreExecute() {
            //TODO: Before Starting the Task
            super.onPreExecute()
        }
        override fun doInBackground(vararg params: String?): String {
            TODO("HTTP Call")
            try {
                //TODO: passing url from fun GetSunset to get executed in the background method
                var url = URL(params[0])

                //TODO: to open http connection
                val urlConnect = url.openConnection() as HttpURLConnection

                //TODO: define your timeout
                urlConnect.connectTimeout = 7000

                var inString = ConvertStreamToString(urlConnect.inputStream)

                //cannot access the UI, hence pass the following method
                publishProgress(inString)

            }
            catch (ex:Exception){

            }

            return null!!
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                var json = JSONObject(values[0])
                var query = json.getJSONObject("query")
                var results = query.getJSONObject("results")
                var channel = results.getJSONObject("channel")
                var astronomy = channel.getJSONObject("astronomy")
                var sunRise = astronomy.getString("sunrise")
                tvSunsetTime.text = "Sunrise time is" +sunRise
            }
            catch (ex:Exception) {

            }
        }

        override fun onPostExecute(result: String?) {
            //TODO: After Task Done.
        }

    }

    //Define Input Stream Fun Out of Scope
    fun ConvertStreamToString(inputStream:InputStream):String {
        var bufferReader = BufferedReader(InputStreamReader(inputStream))

        var line:String
        var AllString:String = ""

        try {
            do {
                line = bufferReader.readLine()
                if (line != null) {
                    AllString += line
                }
            }
            while (line != null)
            inputStream.close()
        }
        catch (ex: Exception) {

        }

        return AllString
    }
}
