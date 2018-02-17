package app.parviz.com.simpleyoutubedownloader

import android.app.Application
import app.parviz.com.simpleyoutubedownloader.di.AppComponent
import app.parviz.com.simpleyoutubedownloader.di.AppModule
import app.parviz.com.simpleyoutubedownloader.di.DaggerAppComponent
import cn.hikyson.android.godeye.toolbox.rxpermission.RxPermissions
import android.app.Activity
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakContextImpl2
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakDetector
import cn.hikyson.godeye.core.internal.modules.pageload.PageloadContextImpl
import cn.hikyson.godeye.core.internal.modules.pageload.Pageload
import cn.hikyson.godeye.core.internal.modules.thread.deadlock.DeadlockDefaultThreadFilter
import cn.hikyson.godeye.core.internal.modules.thread.ThreadDump
import cn.hikyson.godeye.core.GodEye
import cn.hikyson.godeye.core.internal.modules.thread.deadlock.DeadLockContextImpl
import cn.hikyson.godeye.core.internal.modules.thread.deadlock.DeadLock
import cn.hikyson.godeye.core.internal.modules.thread.ThreadContextImpl
import cn.hikyson.android.godeye.toolbox.crash.CrashFileProvider
import cn.hikyson.godeye.core.helper.PermissionRequest
import cn.hikyson.godeye.core.internal.modules.crash.Crash
import cn.hikyson.godeye.core.internal.modules.traffic.TrafficContextImpl
import cn.hikyson.godeye.core.internal.modules.traffic.Traffic
import cn.hikyson.godeye.core.internal.modules.sm.SmContextImpl
import cn.hikyson.godeye.core.internal.modules.sm.Sm
import cn.hikyson.godeye.core.internal.modules.fps.FpsContextImpl
import cn.hikyson.godeye.core.internal.modules.fps.Fps
import cn.hikyson.godeye.core.internal.modules.battery.BatteryContextImpl
import cn.hikyson.godeye.core.internal.modules.battery.Battery
import cn.hikyson.godeye.core.internal.modules.battery.BatteryContext
import cn.hikyson.godeye.core.internal.modules.cpu.CpuContextImpl
import cn.hikyson.godeye.core.internal.modules.cpu.Cpu
import cn.hikyson.godeye.core.internal.modules.cpu.CpuContext
import cn.hikyson.godeye.core.internal.modules.crash.CrashProvider
import cn.hikyson.godeye.core.internal.modules.fps.FpsContext
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakContext
import cn.hikyson.godeye.core.internal.modules.memory.*
import cn.hikyson.godeye.core.internal.modules.pageload.PageloadContext
import cn.hikyson.godeye.core.internal.modules.sm.SmContext
import cn.hikyson.godeye.core.internal.modules.thread.ThreadContext
import cn.hikyson.godeye.core.internal.modules.thread.deadlock.DeadLockContext
import cn.hikyson.godeye.core.internal.modules.traffic.TrafficContext
import io.reactivex.Observable


/**
 * Top level Application object
 */

class App: Application() {

    companion object {
        @JvmStatic lateinit var graph: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDagger()
        initGodEye()
    }

    fun initDagger() {
        graph = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    fun initGodEye() {
        GodEye.instance().install<CpuContext>(Cpu::class.java, CpuContextImpl())
                .install<BatteryContext>(Battery::class.java, BatteryContextImpl(this))
                .install<FpsContext>(Fps::class.java, FpsContextImpl(this))
                .install(Heap::class.java, java.lang.Long.valueOf(2000))
                .install<PssContext>(Pss::class.java, PssContextImpl(this))
                .install<RamContext>(Ram::class.java, RamContextImpl(this))
                .install<SmContext>(Sm::class.java, SmContextImpl(this, 1000, 300, 800))
                .install<TrafficContext>(Traffic::class.java, TrafficContextImpl())
                .install<CrashProvider>(Crash::class.java, CrashFileProvider(this))
                .install<ThreadContext>(ThreadDump::class.java, ThreadContextImpl())
                .install<DeadLockContext>(DeadLock::class.java, DeadLockContextImpl(GodEye.instance().getModule(ThreadDump::class.java).subject(), DeadlockDefaultThreadFilter()))
                .install<PageloadContext>(Pageload::class.java, PageloadContextImpl(this))
                .install<LeakContext>(LeakDetector::class.java, LeakContextImpl2(this,
                        PermissionRequest { activity, permissions -> RxPermissions(activity).request(*permissions) }))
    }
}