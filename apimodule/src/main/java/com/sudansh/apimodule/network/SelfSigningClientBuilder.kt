package com.sudansh.apimodule.network

import android.content.Context
import com.sudansh.apimodule.R
import okhttp3.OkHttpClient
import java.io.IOException
import java.io.InputStream
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

object SelfSigningClientBuilder {

    fun createClient(context: Context): OkHttpClient? {

        var client: OkHttpClient? = null


        try {
            val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
            val cert: InputStream = context.resources.openRawResource(R.raw.certificate)
            val ca: Certificate = cf.generateCertificate(cert)
            val sslContext: SSLContext = SSLContext.getInstance("TLS")

            cert.close()

            val keyStoreType = KeyStore.getDefaultType()
            val keyStore = KeyStore.getInstance(keyStoreType)
            keyStore.load(null, null)
            keyStore.setCertificateEntry("ca", ca)

            val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
            val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
            tmf.init(keyStore)

            sslContext.init(null, tmf.trustManagers, null)

            client = OkHttpClient.Builder()
                .sslSocketFactory(sslContext.socketFactory)
                .build()

        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

        return client
    }

}