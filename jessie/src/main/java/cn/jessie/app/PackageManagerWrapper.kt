package cn.jessie.app

import android.annotation.TargetApi
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.*
import android.content.res.Resources
import android.content.res.XmlResourceParser
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.UserHandle
import cn.jessie.etc.JCLogger
import cn.jessie.etc.Reflections

@Suppress("OverridingDeprecatedMember")
internal open class PackageManagerWrapper(val base: PackageManager) : PackageManager() {
    fun buildRequestPermissionsIntent(permissions: Array<String>): Intent {
        return Reflections.invoke(base, "buildRequestPermissionsIntent", permissions) as Intent
    }

    override fun canonicalToCurrentPackageNames(names: Array<out String>?): Array<String> {
        return base.canonicalToCurrentPackageNames(names)
    }

    override fun getLaunchIntentForPackage(packageName: String): Intent? {
        return base.getLaunchIntentForPackage(packageName)
    }

    override fun getResourcesForApplication(app: ApplicationInfo?): Resources {
        return base.getResourcesForApplication(app)
    }

    override fun getResourcesForApplication(appPackageName: String?): Resources {
        return base.getResourcesForApplication(appPackageName)
    }

    open fun getResourcesForApplicationAsUser(appPackageName: String?, userId: Int): Resources {
        return Reflections.invoke(base, "getResourcesForApplicationAsUser", appPackageName, userId) as Resources
    }

    override fun getProviderInfo(component: ComponentName?, flags: Int): ProviderInfo {
        return base.getProviderInfo(component, flags)
    }

    override fun getReceiverInfo(component: ComponentName?, flags: Int): ActivityInfo {
        return base.getReceiverInfo(component, flags)
    }

    override fun queryIntentActivityOptions(caller: ComponentName?, specifics: Array<Intent>?, intent: Intent?, flags: Int): MutableList<ResolveInfo> {
        return base.queryIntentActivityOptions(caller, specifics, intent, flags)
    }

    override fun clearPackagePreferredActivities(packageName: String?) {
        return base.clearPackagePreferredActivities(packageName)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getPackageInstaller(): PackageInstaller {
        return base.getPackageInstaller()
    }

    override fun resolveService(intent: Intent?, flags: Int): ResolveInfo? {
        return base.resolveService(intent, flags)
    }

    override fun verifyPendingInstall(id: Int, verificationCode: Int) {
        return base.verifyPendingInstall(id, verificationCode)
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun getInstantAppCookie(): ByteArray {
        return base.getInstantAppCookie()
    }

    override fun getApplicationIcon(info: ApplicationInfo?): Drawable {
        return base.getApplicationIcon(info)
    }

    override fun getApplicationIcon(packageName: String?): Drawable {
        return base.getApplicationIcon(packageName)
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun extendVerificationTimeout(id: Int, verificationCodeAtTimeout: Int, millisecondsToDelay: Long) {
        return base.extendVerificationTimeout(id, verificationCodeAtTimeout, millisecondsToDelay)
    }

    override fun getText(packageName: String, resid: Int, appInfo: ApplicationInfo?): CharSequence {
        return base.getText(packageName, resid, appInfo)
    }

    override fun resolveContentProvider(name: String, flags: Int): ProviderInfo? {
        return base.resolveContentProvider(name, flags)
    }

    override fun getApplicationEnabledSetting(packageName: String): Int {
        return base.getApplicationEnabledSetting(packageName)
    }

    override fun queryIntentServices(intent: Intent?, flags: Int): MutableList<ResolveInfo> {
        return base.queryIntentServices(intent, flags)
    }

    override fun hasSystemFeature(name: String?): Boolean {
        return base.hasSystemFeature(name)
    }

    @TargetApi(Build.VERSION_CODES.N)
    override fun hasSystemFeature(name: String?, version: Int): Boolean {
        return base.hasSystemFeature(name, version)
    }

    override fun getInstrumentationInfo(className: ComponentName?, flags: Int): InstrumentationInfo {
        return base.getInstrumentationInfo(className, flags)
    }

    override fun getInstalledApplications(flags: Int): MutableList<ApplicationInfo> {
        return base.getInstalledApplications(flags)
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun isPermissionRevokedByPolicy(permName: String, pkgName: String): Boolean {
        return base.isPermissionRevokedByPolicy(permName, pkgName)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getUserBadgedDrawableForDensity(drawable: Drawable?, user: UserHandle?, badgeLocation: Rect?, badgeDensity: Int): Drawable {
        return base.getUserBadgedDrawableForDensity(drawable, user, badgeLocation, badgeDensity)
    }

    fun getUserBadgeForDensity(user: UserHandle?, density: Int): Drawable? {
        return Reflections.invoke(base, "getUserBadgeForDensity", user, density) as? Drawable
    }

    override fun checkPermission(permName: String?, pkgName: String?): Int {
        return base.checkPermission(permName, pkgName)
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun getInstantAppCookieMaxBytes(): Int {
        return base.getInstantAppCookieMaxBytes()
    }

    override fun getDefaultActivityIcon(): Drawable {
        return base.getDefaultActivityIcon()
    }

    override fun getPreferredPackages(flags: Int): MutableList<PackageInfo> {
        return base.getPreferredPackages(flags)
    }

    override fun checkSignatures(pkg1: String?, pkg2: String?): Int {
        return base.checkSignatures(pkg1, pkg2)
    }

    override fun checkSignatures(uid1: Int, uid2: Int): Int {
        return base.checkSignatures(uid1, uid2)
    }

    override fun addPreferredActivity(filter: IntentFilter?, match: Int, set: Array<out ComponentName>?, activity: ComponentName?) {
        return base.addPreferredActivity(filter, match, set, activity)
    }

    override fun removePackageFromPreferred(packageName: String?) {
        return base.removePackageFromPreferred(packageName)
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun getSharedLibraries(flags: Int): MutableList<SharedLibraryInfo> {
        return base.getSharedLibraries(flags)
    }

    override fun queryIntentActivities(intent: Intent?, flags: Int): MutableList<ResolveInfo> {
        return base.queryIntentActivities(intent, flags) ?: mutableListOf()
    }

    override fun addPermission(info: PermissionInfo?): Boolean {
        return base.addPermission(info)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun getActivityBanner(activityName: ComponentName?): Drawable {
        return base.getActivityBanner(activityName)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun getActivityBanner(intent: Intent?): Drawable {
        return base.getActivityBanner(intent)
    }

    override fun getDrawable(packageName: String?, resid: Int, appInfo: ApplicationInfo?): Drawable {
        return base.getDrawable(packageName, resid, appInfo)
    }

    override fun setComponentEnabledSetting(componentName: ComponentName, newState: Int, flags: Int) {
        return base.setComponentEnabledSetting(componentName, newState, flags)
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun getChangedPackages(sequenceNumber: Int): ChangedPackages? {
        return base.getChangedPackages(sequenceNumber)
    }

    override fun getApplicationInfo(packageName: String?, flags: Int): ApplicationInfo {
        return base.getApplicationInfo(packageName, flags)
    }

    override fun resolveActivity(intent: Intent?, flags: Int): ResolveInfo? {
        return base.resolveActivity(intent, flags)
    }

    override fun queryBroadcastReceivers(intent: Intent?, flags: Int): MutableList<ResolveInfo> {
        return base.queryBroadcastReceivers(intent, flags)
    }

    override fun getXml(packageName: String, resid: Int, appInfo: ApplicationInfo): XmlResourceParser {
        return base.getXml(packageName, resid, appInfo)
    }

    override fun getPackageInfo(packageName: String?, flags: Int): PackageInfo {
        return base.getPackageInfo(packageName, flags)
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun getPackageInfo(versionedPackage: VersionedPackage?, flags: Int): PackageInfo {
        return base.getPackageInfo(versionedPackage, flags)
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun getPackagesHoldingPermissions(permissions: Array<out String>?, flags: Int): MutableList<PackageInfo> {
        return base.getPackagesHoldingPermissions(permissions, flags)
    }

    override fun addPermissionAsync(info: PermissionInfo?): Boolean {
        return base.addPermissionAsync(info)
    }

    override fun getSystemAvailableFeatures(): Array<FeatureInfo> {
        return base.getSystemAvailableFeatures()
    }

    override fun getActivityLogo(activityName: ComponentName?): Drawable {
        return base.getActivityLogo(activityName)
    }

    override fun getActivityLogo(intent: Intent?): Drawable {
        return base.getActivityLogo(intent)
    }

    override fun getSystemSharedLibraryNames(): Array<String> {
        return base.getSystemSharedLibraryNames()
    }

    override fun queryPermissionsByGroup(group: String?, flags: Int): MutableList<PermissionInfo> {
        return base.queryPermissionsByGroup(group, flags)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    override fun queryIntentContentProviders(intent: Intent?, flags: Int): MutableList<ResolveInfo> {
        return base.queryIntentContentProviders(intent, flags)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun getApplicationBanner(info: ApplicationInfo?): Drawable {
        return base.getApplicationBanner(info)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun getApplicationBanner(packageName: String?): Drawable {
        return base.getApplicationBanner(packageName)
    }

    override fun queryContentProviders(processName: String?, uid: Int, flags: Int): MutableList<ProviderInfo> {
        return base.queryContentProviders(processName, uid, flags)
    }

    override fun getPackageGids(packageName: String): IntArray {
        return base.getPackageGids(packageName)
    }

    @TargetApi(Build.VERSION_CODES.N)
    override fun getPackageGids(packageName: String?, flags: Int): IntArray {
        return base.getPackageGids(packageName, flags)
    }

    override fun getResourcesForActivity(activityName: ComponentName?): Resources {
        return base.getResourcesForActivity(activityName)
    }

    override fun getPackagesForUid(uid: Int): Array<String>? {
        return base.getPackagesForUid(uid)
    }

    override fun getPermissionGroupInfo(name: String?, flags: Int): PermissionGroupInfo {
        return base.getPermissionGroupInfo(name, flags)
    }

    override fun getPermissionInfo(name: String?, flags: Int): PermissionInfo {
        return base.getPermissionInfo(name, flags)
    }

    override fun removePermission(name: String?) {
        return base.removePermission(name)
    }

    override fun queryInstrumentation(targetPackage: String?, flags: Int): MutableList<InstrumentationInfo> {
        return base.queryInstrumentation(targetPackage, flags)
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun clearInstantAppCookie() {
        return base.clearInstantAppCookie()
    }

    override fun addPackageToPreferred(packageName: String?) {
        return base.addPackageToPreferred(packageName)
    }

    override fun currentToCanonicalPackageNames(names: Array<out String>?): Array<String> {
        return base.currentToCanonicalPackageNames(names)
    }

    @TargetApi(Build.VERSION_CODES.N)
    override fun getPackageUid(packageName: String?, flags: Int): Int {
        return base.getPackageUid(packageName, flags)
    }

    override fun getComponentEnabledSetting(componentName: ComponentName?): Int {
        return try {
            base.getComponentEnabledSetting(componentName)
        } catch (e: Throwable) {
            JCLogger.error(e)
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getLeanbackLaunchIntentForPackage(packageName: String): Intent? {
        return base.getLeanbackLaunchIntentForPackage(packageName)
    }

    override fun getInstalledPackages(flags: Int): MutableList<PackageInfo> {
        return base.getInstalledPackages(flags)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getUserBadgedIcon(icon: Drawable?, user: UserHandle?): Drawable {
        return base.getUserBadgedIcon(icon, user)
    }

    override fun getAllPermissionGroups(flags: Int): MutableList<PermissionGroupInfo> {
        return base.getAllPermissionGroups(flags)
    }

    override fun getActivityInfo(component: ComponentName?, flags: Int): ActivityInfo {
        return base.getActivityInfo(component, flags)
    }

    override fun getNameForUid(uid: Int): String? {
        return base.getNameForUid(uid)
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun updateInstantAppCookie(cookie: ByteArray?) {
        return base.updateInstantAppCookie(cookie)
    }

    override fun getApplicationLogo(info: ApplicationInfo?): Drawable {
        return base.getApplicationLogo(info)
    }

    override fun getApplicationLogo(packageName: String?): Drawable {
        return base.getApplicationLogo(packageName)
    }

    override fun getApplicationLabel(info: ApplicationInfo?): CharSequence {
        return base.getApplicationLabel(info)
    }

    override fun getPreferredActivities(outFilters: MutableList<IntentFilter>, outActivities: MutableList<ComponentName>, packageName: String?): Int {
        return base.getPreferredActivities(outFilters, outActivities, packageName)
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun setApplicationCategoryHint(packageName: String, categoryHint: Int) {
        return base.setApplicationCategoryHint(packageName, categoryHint)
    }

    override fun isSafeMode(): Boolean {
        return base.isSafeMode()
    }

    override fun setInstallerPackageName(targetPackage: String?, installerPackageName: String?) {
        return base.setInstallerPackageName(targetPackage, installerPackageName)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getUserBadgedLabel(label: CharSequence?, user: UserHandle?): CharSequence {
        return base.getUserBadgedLabel(label, user)
    }

    override fun getInstallerPackageName(packageName: String?): String {
        return base.getInstallerPackageName(packageName)
    }

    override fun setApplicationEnabledSetting(packageName: String, newState: Int, flags: Int) {
        return base.setApplicationEnabledSetting(packageName, newState, flags)
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun canRequestPackageInstalls(): Boolean {
        return base.canRequestPackageInstalls()
    }

    override fun getServiceInfo(component: ComponentName?, flags: Int): ServiceInfo {
        return base.getServiceInfo(component, flags)
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun isInstantApp(): Boolean {
        return base.isInstantApp()
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun isInstantApp(packageName: String?): Boolean {
        return base.isInstantApp()
    }

    override fun getActivityIcon(activityName: ComponentName?): Drawable {
        return base.getActivityIcon(activityName)
    }

    override fun getActivityIcon(intent: Intent?): Drawable {
        return base.getActivityIcon(intent)
    }
}