package cn.jessie.stub

import android.app.Activity
import android.app.ActivityWrapper
import android.app.Service
import android.app.ServiceWrapper
import android.content.ContentProvider
import android.content.ContentProviderWrapper
import cn.jessie.JessieRuntime

object JessieStub {
    private const val STUB_ACTIVITY = "cn.jessie.runtime.app.activity.JessieStubActivity"
    private const val STUB_SERVICE = "cn.jessie.runtime.app.service.JessieStubService"
    private const val STUB_PROVIDER = "cn.jessie.runtime.app.provider.JessieStubProvider"

    abstract class BaseActivity : ActivityWrapper() {
        override val baseActivity by lazy { JessieRuntime.create<Activity>(STUB_ACTIVITY) }
    }

    abstract class BaseService : ServiceWrapper() {
        override val baseService by lazy { JessieRuntime.create<Service>(STUB_SERVICE) }
    }

    abstract class BaseProvider : ContentProviderWrapper() {
        override val baseProvider by lazy { JessieRuntime.create<ContentProvider>(STUB_PROVIDER) }
    }
}