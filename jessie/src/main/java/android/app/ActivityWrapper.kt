package android.app

import android.annotation.SuppressLint
import android.app.assist.AssistContent
import android.content.*
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal
import android.os.PersistableBundle
import android.transition.Scene
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.*
import android.view.accessibility.AccessibilityEvent
import android.widget.Toolbar
import cn.jessie.etc.ReflectionUtils
import java.io.FileDescriptor
import java.io.PrintWriter
import java.util.function.Consumer

@SuppressLint("MissingPermission", "NewApi", "MissingSuperCall")
abstract class ActivityWrapper : Activity() {
    abstract val baseActivity: Activity

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return baseActivity.onKeyDown(keyCode, event)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return baseActivity.onContextItemSelected(item)
    }

    override fun setExitSharedElementCallback(callback: SharedElementCallback?) {
        baseActivity.setExitSharedElementCallback(callback)
    }

    override fun onAttachedToWindow() {
        baseActivity.onAttachedToWindow()
    }

    override fun startNextMatchingActivity(intent: Intent): Boolean {
        return baseActivity.startNextMatchingActivity(intent)
    }

    override fun startNextMatchingActivity(intent: Intent, options: Bundle?): Boolean {
        return baseActivity.startNextMatchingActivity(intent, options)
    }

    override fun <T : View?> findViewById(id: Int): T {
        return baseActivity.findViewById<T>(id)
    }

    override fun startIntentSenderForResult(intent: IntentSender?, requestCode: Int, fillInIntent: Intent?, flagsMask: Int, flagsValues: Int, extraFlags: Int) {
        baseActivity.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags)
    }

    override fun startIntentSenderForResult(intent: IntentSender?, requestCode: Int, fillInIntent: Intent?, flagsMask: Int, flagsValues: Int, extraFlags: Int, options: Bundle?) {
        baseActivity.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options)
    }

    override fun hasWindowFocus(): Boolean {
        return baseActivity.hasWindowFocus()
    }

    override fun setPictureInPictureParams(params: PictureInPictureParams) {
        baseActivity.setPictureInPictureParams(params)
    }

    override fun isImmersive(): Boolean {
        return baseActivity.isImmersive()
    }

    override fun isVoiceInteraction(): Boolean {
        return baseActivity.isVoiceInteraction()
    }

    override fun onProvideAssistData(data: Bundle?) {
        baseActivity.onProvideAssistData(data)
    }

    override fun registerActivityLifecycleCallbacks(callback: Application.ActivityLifecycleCallbacks) {
        baseActivity.registerActivityLifecycleCallbacks(callback)
    }

    override fun onPause() {
        super.onPause()
        baseActivity.onPause()
    }

    override fun getMenuInflater(): MenuInflater {
        return baseActivity.getMenuInflater()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return baseActivity.onTouchEvent(event)
    }

    override fun takeKeyEvents(get: Boolean) {
        baseActivity.takeKeyEvents(get)
    }

    override fun onPreparePanel(featureId: Int, view: View?, menu: Menu): Boolean {
        return baseActivity.onPreparePanel(featureId, view, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return baseActivity.onPrepareOptionsMenu(menu)
    }

    override fun setEnterSharedElementCallback(callback: SharedElementCallback?) {
        baseActivity.setEnterSharedElementCallback(callback)
    }

    override fun startActivityFromFragment(fragment: Fragment, intent: Intent?, requestCode: Int) {
        baseActivity.startActivityFromFragment(fragment, intent, requestCode)
    }

    override fun startActivityFromFragment(fragment: Fragment, intent: Intent?, requestCode: Int, options: Bundle?) {
        baseActivity.startActivityFromFragment(fragment, intent, requestCode, options)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        baseActivity.onActivityResult(requestCode, resultCode, data)
    }

    override fun onActionModeFinished(mode: ActionMode?) {
        super.onActionModeFinished(mode)
        baseActivity.onActionModeFinished(mode)
    }

    override fun getLayoutInflater(): LayoutInflater {
        return baseActivity.getLayoutInflater()
    }

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean, newConfig: Configuration?) {
        baseActivity.onMultiWindowModeChanged(isInMultiWindowMode, newConfig)
    }

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean) {
        baseActivity.onMultiWindowModeChanged(isInMultiWindowMode)
    }

    override fun getLastNonConfigurationInstance(): Any? {
        return baseActivity.getLastNonConfigurationInstance()
    }

    override fun onEnterAnimationComplete() {
        baseActivity.onEnterAnimationComplete()
    }

    override fun onKeyShortcut(keyCode: Int, event: KeyEvent?): Boolean {
        return baseActivity.onKeyShortcut(keyCode, event)
    }

    override fun setInheritShowWhenLocked(inheritShowWhenLocked: Boolean) {
        baseActivity.setInheritShowWhenLocked(inheritShowWhenLocked)
    }

    override fun dispatchKeyShortcutEvent(event: KeyEvent?): Boolean {
        return baseActivity.dispatchKeyShortcutEvent(event)
    }

    override fun unregisterActivityLifecycleCallbacks(callback: Application.ActivityLifecycleCallbacks) {
        baseActivity.unregisterActivityLifecycleCallbacks(callback)
    }

    override fun setShowWhenLocked(showWhenLocked: Boolean) {
        baseActivity.setShowWhenLocked(showWhenLocked)
    }

    override fun onCreateNavigateUpTaskStack(builder: TaskStackBuilder?) {
        baseActivity.onCreateNavigateUpTaskStack(builder)
    }

    override fun registerForContextMenu(view: View?) {
        baseActivity.registerForContextMenu(view)
    }

    override fun getTaskId(): Int {
        return baseActivity.getTaskId()
    }

    override fun onContextMenuClosed(menu: Menu) {
        baseActivity.onContextMenuClosed(menu)
    }

    override fun getCallingActivity(): ComponentName? {
        return baseActivity.getCallingActivity()
    }

    override fun onNavigateUp(): Boolean {
        return baseActivity.onNavigateUp()
    }

    override fun postponeEnterTransition() {
        baseActivity.postponeEnterTransition()
    }

    override fun onPerformDirectAction(actionId: String, arguments: Bundle, cancellationSignal: CancellationSignal, resultListener: Consumer<Bundle>) {
        baseActivity.onPerformDirectAction(actionId, arguments, cancellationSignal, resultListener)
    }

    override fun onCreateDescription(): CharSequence? {
        return baseActivity.onCreateDescription()
    }

    override fun isTaskRoot(): Boolean {
        return baseActivity.isTaskRoot()
    }

    override fun onPostResume() {
        super.onPostResume()
        baseActivity.onPostResume()
    }

    override fun stopManagingCursor(c: Cursor?) {
        baseActivity.stopManagingCursor(c)
    }

    override fun getMaxNumPictureInPictureActions(): Int {
        return baseActivity.getMaxNumPictureInPictureActions()
    }

    override fun addContentView(view: View?, params: ViewGroup.LayoutParams?) {
        baseActivity.addContentView(view, params)
    }

    override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return baseActivity.shouldShowRequestPermissionRationale(permission)
    }

    override fun triggerSearch(query: String?, appSearchData: Bundle?) {
        baseActivity.triggerSearch(query, appSearchData)
    }

    override fun setTaskDescription(taskDescription: ActivityManager.TaskDescription?) {
        baseActivity.setTaskDescription(taskDescription)
    }

    override fun isLocalVoiceInteractionSupported(): Boolean {
        return baseActivity.isLocalVoiceInteractionSupported()
    }

    override fun onTrimMemory(level: Int) {
        baseActivity.onTrimMemory(level)
    }

    override fun startLockTask() {
        baseActivity.startLockTask()
    }

    override fun setTurnScreenOn(turnScreenOn: Boolean) {
        baseActivity.setTurnScreenOn(turnScreenOn)
    }

    override fun onProvideKeyboardShortcuts(data: MutableList<KeyboardShortcutGroup>?, menu: Menu?, deviceId: Int) {
        baseActivity.onProvideKeyboardShortcuts(data, menu, deviceId)
    }

    override fun setContentTransitionManager(tm: TransitionManager?) {
        baseActivity.setContentTransitionManager(tm)
    }

    override fun onAttachFragment(fragment: Fragment?) {
        baseActivity.onAttachFragment(fragment)
    }

    override fun setVisible(visible: Boolean) {
        baseActivity.setVisible(visible)
    }

    override fun isChangingConfigurations(): Boolean {
        return baseActivity.isChangingConfigurations()
    }

    override fun finishActivity(requestCode: Int) {
        baseActivity.finishActivity(requestCode)
    }

    override fun onActionModeStarted(mode: ActionMode?) {
        super.onActionModeStarted(mode)
        baseActivity.onActionModeStarted(mode)
    }

    override fun isInMultiWindowMode(): Boolean {
        return baseActivity.isInMultiWindowMode()
    }

    override fun onTopResumedActivityChanged(isTopResumedActivity: Boolean) {
        baseActivity.onTopResumedActivityChanged(isTopResumedActivity)
    }

    override fun setTitleColor(textColor: Int) {
        baseActivity.setTitleColor(textColor)
    }

    override fun startActionMode(callback: ActionMode.Callback?): ActionMode? {
        return baseActivity.startActionMode(callback)
    }

    override fun startActionMode(callback: ActionMode.Callback?, type: Int): ActionMode? {
        return baseActivity.startActionMode(callback, type)
    }

    override fun setContentView(layoutResID: Int) {
        baseActivity.setContentView(layoutResID)
    }

    override fun setContentView(view: View?) {
        baseActivity.setContentView(view)
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        baseActivity.setContentView(view, params)
    }

    override fun stopLockTask() {
        baseActivity.stopLockTask()
    }

    override fun onUserInteraction() {
        baseActivity.onUserInteraction()
    }

    override fun setImmersive(i: Boolean) {
        baseActivity.setImmersive(i)
    }

    override fun getChangingConfigurations(): Int {
        return baseActivity.getChangingConfigurations()
    }

    override fun onKeyMultiple(keyCode: Int, repeatCount: Int, event: KeyEvent?): Boolean {
        return baseActivity.onKeyMultiple(keyCode, repeatCount, event)
    }

    override fun shouldUpRecreateTask(targetIntent: Intent?): Boolean {
        return baseActivity.shouldUpRecreateTask(targetIntent)
    }

    override fun navigateUpTo(upIntent: Intent?): Boolean {
        return baseActivity.navigateUpTo(upIntent)
    }

    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        return baseActivity.onMenuOpened(featureId, menu)
    }

    override fun onPanelClosed(featureId: Int, menu: Menu) {
        baseActivity.onPanelClosed(featureId, menu)
    }

    override fun isInPictureInPictureMode(): Boolean {
        return baseActivity.isInPictureInPictureMode()
    }

    override fun finish() {
        baseActivity.finish()
    }

    override fun finishAffinity() {
        baseActivity.finishAffinity()
    }

    override fun onCreateDialog(id: Int): Dialog {
        return baseActivity.onCreateDialog(id)
    }

    override fun onCreateDialog(id: Int, args: Bundle?): Dialog? {
        return baseActivity.onCreateDialog(id, args)
    }

    override fun startActivityFromChild(child: Activity, intent: Intent?, requestCode: Int) {
        baseActivity.startActivityFromChild(child, intent, requestCode)
    }

    override fun startActivityFromChild(child: Activity, intent: Intent?, requestCode: Int, options: Bundle?) {
        baseActivity.startActivityFromChild(child, intent, requestCode, options)
    }

    override fun enterPictureInPictureMode() {
        baseActivity.enterPictureInPictureMode()
    }

    override fun enterPictureInPictureMode(params: PictureInPictureParams): Boolean {
        return baseActivity.enterPictureInPictureMode(params)
    }

    override fun finishFromChild(child: Activity?) {
        baseActivity.finishFromChild(child)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        baseActivity.onWindowFocusChanged(hasFocus)
    }

    override fun moveTaskToBack(nonRoot: Boolean): Boolean {
        return baseActivity.moveTaskToBack(nonRoot)
    }

    override fun finishActivityFromChild(child: Activity, requestCode: Int) {
        baseActivity.finishActivityFromChild(child, requestCode)
    }

    override fun onDestroy() {
        super.onDestroy()
        baseActivity.onDestroy()
    }

    override fun getActionBar(): ActionBar? {
        return baseActivity.getActionBar()
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        baseActivity.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun getContentTransitionManager(): TransitionManager {
        return baseActivity.getContentTransitionManager()
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        baseActivity.onActivityReenter(resultCode, data)
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        return baseActivity.onKeyLongPress(keyCode, event)
    }

    override fun onBackPressed() {
        baseActivity.onBackPressed()
    }

    override fun onLocalVoiceInteractionStopped() {
        baseActivity.onLocalVoiceInteractionStopped()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        baseActivity.onConfigurationChanged(newConfig)
    }

    override fun navigateUpToFromChild(child: Activity?, upIntent: Intent?): Boolean {
        return baseActivity.navigateUpToFromChild(child, upIntent)
    }

    override fun setActionBar(toolbar: Toolbar?) {
        baseActivity.setActionBar(toolbar)
    }

    override fun onVisibleBehindCanceled() {
        super.onVisibleBehindCanceled()
        baseActivity.onVisibleBehindCanceled()
    }

    override fun openContextMenu(view: View?) {
        baseActivity.openContextMenu(view)
    }

    override fun getParentActivityIntent(): Intent? {
        return baseActivity.getParentActivityIntent()
    }

    override fun startIntentSender(intent: IntentSender?, fillInIntent: Intent?, flagsMask: Int, flagsValues: Int, extraFlags: Int) {
        baseActivity.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags)
    }

    override fun startIntentSender(intent: IntentSender?, fillInIntent: Intent?, flagsMask: Int, flagsValues: Int, extraFlags: Int, options: Bundle?) {
        baseActivity.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags, options)
    }

    override fun closeContextMenu() {
        baseActivity.closeContextMenu()
    }

    override fun onPrepareDialog(id: Int, dialog: Dialog?) {
        baseActivity.onPrepareDialog(id, dialog)
    }

    override fun onPrepareDialog(id: Int, dialog: Dialog?, args: Bundle?) {
        baseActivity.onPrepareDialog(id, dialog, args)
    }

    override fun onNewIntent(intent: Intent?) {
        baseActivity.onNewIntent(intent)
    }

    override fun getRequestedOrientation(): Int {
        return baseActivity.getRequestedOrientation()
    }

    override fun getVoiceInteractor(): VoiceInteractor {
        return baseActivity.getVoiceInteractor()
    }

    override fun dump(prefix: String, fd: FileDescriptor?, writer: PrintWriter, args: Array<String>?) {
        baseActivity.dump(prefix, fd, writer, args)
    }

    override fun onContentChanged() {
        baseActivity.onContentChanged()
    }

    override fun releaseInstance(): Boolean {
        return baseActivity.releaseInstance()
    }

    override fun onSearchRequested(searchEvent: SearchEvent?): Boolean {
        return baseActivity.onSearchRequested(searchEvent)
    }

    override fun onSearchRequested(): Boolean {
        return baseActivity.onSearchRequested()
    }

    override fun onNavigateUpFromChild(child: Activity?): Boolean {
        return baseActivity.onNavigateUpFromChild(child)
    }

    override fun getReferrer(): Uri? {
        return baseActivity.getReferrer()
    }

    override fun startLocalVoiceInteraction(privateOptions: Bundle?) {
        baseActivity.startLocalVoiceInteraction(privateOptions)
    }

    override fun setRequestedOrientation(requestedOrientation: Int) {
        baseActivity.setRequestedOrientation(requestedOrientation)
    }

    override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent?): Boolean {
        return baseActivity.dispatchPopulateAccessibilityEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ReflectionUtils.copyFields(Activity::class.java, this, baseActivity)
        baseActivity.onCreate(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        baseActivity.onCreate(savedInstanceState, persistentState)
    }

    override fun onLowMemory() {
        baseActivity.onLowMemory()
    }

    override fun reportFullyDrawn() {
        baseActivity.reportFullyDrawn()
    }

    override fun onRetainNonConfigurationInstance(): Any {
        return baseActivity.onRetainNonConfigurationInstance()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return baseActivity.onCreateOptionsMenu(menu)
    }

    override fun startPostponedEnterTransition() {
        baseActivity.startPostponedEnterTransition()
    }

    override fun getLoaderManager(): LoaderManager {
        return baseActivity.getLoaderManager()
    }

    override fun isActivityTransitionRunning(): Boolean {
        return baseActivity.isActivityTransitionRunning()
    }

    override fun onStart() {
        super.onStart()
        baseActivity.onStart()
    }

    override fun unregisterForContextMenu(view: View?) {
        baseActivity.unregisterForContextMenu(view)
    }

    override fun overridePendingTransition(enterAnim: Int, exitAnim: Int) {
        baseActivity.overridePendingTransition(enterAnim, exitAnim)
    }

    override fun onGenericMotionEvent(event: MotionEvent?): Boolean {
        return baseActivity.onGenericMotionEvent(event)
    }

    override fun onProvideAssistContent(outContent: AssistContent?) {
        baseActivity.onProvideAssistContent(outContent)
    }

    override fun stopLocalVoiceInteraction() {
        baseActivity.stopLocalVoiceInteraction()
    }

    override fun recreate() {
        baseActivity.recreate()
    }

    override fun onOptionsMenuClosed(menu: Menu?) {
        baseActivity.onOptionsMenuClosed(menu)
    }

    override fun createPendingResult(requestCode: Int, data: Intent, flags: Int): PendingIntent {
        return baseActivity.createPendingResult(requestCode, data, flags)
    }

    override fun onWindowAttributesChanged(params: WindowManager.LayoutParams?) {
        baseActivity.onWindowAttributesChanged(params)
    }

    override fun startActivities(intents: Array<out Intent>?) {
        baseActivity.startActivities(intents)
    }

    override fun startActivities(intents: Array<out Intent>?, options: Bundle?) {
        baseActivity.startActivities(intents, options)
    }

    override fun onUserLeaveHint() {
        baseActivity.onUserLeaveHint()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        baseActivity.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun startManagingCursor(c: Cursor?) {
        baseActivity.startManagingCursor(c)
    }

    override fun onResume() {
        super.onResume()
        baseActivity.onResume()
    }

    override fun onTitleChanged(title: CharSequence?, color: Int) {
        baseActivity.onTitleChanged(title, color)
    }

    override fun onProvideReferrer(): Uri {
        return baseActivity.onProvideReferrer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        baseActivity.onSaveInstanceState(outState)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        baseActivity.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onChildTitleChanged(childActivity: Activity?, title: CharSequence?) {
        baseActivity.onChildTitleChanged(childActivity, title)
    }

    override fun onStateNotSaved() {
        baseActivity.onStateNotSaved()
    }

    override fun dispatchGenericMotionEvent(ev: MotionEvent?): Boolean {
        return baseActivity.dispatchGenericMotionEvent(ev)
    }

    override fun openOptionsMenu() {
        baseActivity.openOptionsMenu()
    }

    override fun dispatchTrackballEvent(ev: MotionEvent?): Boolean {
        return baseActivity.dispatchTrackballEvent(ev)
    }

    override fun setTitle(title: CharSequence?) {
        baseActivity.setTitle(title)
    }

    override fun setTitle(titleId: Int) {
        baseActivity.setTitle(titleId)
    }

    override fun isVoiceInteractionRoot(): Boolean {
        return baseActivity.isVoiceInteractionRoot()
    }

    override fun startActivity(intent: Intent?) {
        baseActivity.startActivity(intent)
    }

    override fun startActivity(intent: Intent?, options: Bundle?) {
        baseActivity.startActivity(intent, options)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return baseActivity.onCreateView(name, context, attrs)
    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        return baseActivity.onCreateView(parent, name, context, attrs)
    }

    override fun requestDragAndDropPermissions(event: DragEvent?): DragAndDropPermissions {
        return baseActivity.requestDragAndDropPermissions(event)
    }

    override fun isDestroyed(): Boolean {
        return baseActivity.isDestroyed()
    }

    override fun setVrModeEnabled(enabled: Boolean, requestedComponent: ComponentName) {
        baseActivity.setVrModeEnabled(enabled, requestedComponent)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        baseActivity.onPostCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        baseActivity.onPostCreate(savedInstanceState, persistentState)
    }

    override fun getTheme(): Resources.Theme {
        return baseActivity.getTheme()
    }

    override fun onApplyThemeResource(theme: Resources.Theme?, resid: Int, first: Boolean) {
        baseActivity.onApplyThemeResource(theme, resid, first)
    }

    override fun startIntentSenderFromChild(child: Activity?, intent: IntentSender?, requestCode: Int, fillInIntent: Intent?, flagsMask: Int, flagsValues: Int, extraFlags: Int) {
        baseActivity.startIntentSenderFromChild(child, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags)
    }

    override fun startIntentSenderFromChild(child: Activity?, intent: IntentSender?, requestCode: Int, fillInIntent: Intent?, flagsMask: Int, flagsValues: Int, extraFlags: Int, options: Bundle?) {
        baseActivity.startIntentSenderFromChild(child, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options)
    }

    override fun onLocalVoiceInteractionStarted() {
        baseActivity.onLocalVoiceInteractionStarted()
    }

    override fun onGetDirectActions(cancellationSignal: CancellationSignal, callback: Consumer<MutableList<DirectAction>>) {
        baseActivity.onGetDirectActions(cancellationSignal, callback)
    }

    override fun onTrackballEvent(event: MotionEvent?): Boolean {
        return baseActivity.onTrackballEvent(event)
    }

    override fun onDetachedFromWindow() {
        baseActivity.onDetachedFromWindow()
    }

    override fun closeOptionsMenu() {
        baseActivity.closeOptionsMenu()
    }

    override fun finishAndRemoveTask() {
        baseActivity.finishAndRemoveTask()
    }

    override fun requestVisibleBehind(visible: Boolean): Boolean {
        return baseActivity.requestVisibleBehind(visible)
    }

    override fun onCreatePanelView(featureId: Int): View? {
        return baseActivity.onCreatePanelView(featureId)
    }

    override fun onPrepareNavigateUpTaskStack(builder: TaskStackBuilder?) {
        baseActivity.onPrepareNavigateUpTaskStack(builder)
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        baseActivity.startActivityForResult(intent, requestCode)
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        baseActivity.startActivityForResult(intent, requestCode, options)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return baseActivity.dispatchTouchEvent(ev)
    }

    override fun onCreatePanelMenu(featureId: Int, menu: Menu): Boolean {
        return baseActivity.onCreatePanelMenu(featureId, menu)
    }

    override fun onWindowStartingActionMode(callback: ActionMode.Callback?): ActionMode? {
        return baseActivity.onWindowStartingActionMode(callback)
    }

    override fun onWindowStartingActionMode(callback: ActionMode.Callback?, type: Int): ActionMode? {
        return baseActivity.onWindowStartingActionMode(callback, type)
    }

    override fun getLocalClassName(): String {
        return baseActivity.getLocalClassName()
    }

    override fun getPreferences(mode: Int): SharedPreferences {
        return baseActivity.getPreferences(mode)
    }

    override fun onStop() {
        super.onStop()
        baseActivity.onStop()
    }

    override fun getCurrentFocus(): View? {
        return baseActivity.getCurrentFocus()
    }

    override fun onRestart() {
        super.onRestart()
        baseActivity.onRestart()
    }

    override fun startActivityIfNeeded(intent: Intent, requestCode: Int): Boolean {
        return baseActivity.startActivityIfNeeded(intent, requestCode)
    }

    override fun startActivityIfNeeded(intent: Intent, requestCode: Int, options: Bundle?): Boolean {
        return baseActivity.startActivityIfNeeded(intent, requestCode, options)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return baseActivity.onOptionsItemSelected(item)
    }

    override fun setIntent(newIntent: Intent?) {
        baseActivity.setIntent(newIntent)
    }

    override fun getFragmentManager(): FragmentManager {
        return baseActivity.getFragmentManager()
    }

    override fun getCallingPackage(): String? {
        return baseActivity.getCallingPackage()
    }

    override fun showAssist(args: Bundle?): Boolean {
        return baseActivity.showAssist(args)
    }

    override fun startSearch(initialQuery: String?, selectInitialQuery: Boolean, appSearchData: Bundle?, globalSearch: Boolean) {
        baseActivity.startSearch(initialQuery, selectInitialQuery, appSearchData, globalSearch)
    }

    override fun finishAfterTransition() {
        baseActivity.finishAfterTransition()
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        return baseActivity.dispatchKeyEvent(event)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        baseActivity.onRestoreInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        baseActivity.onRestoreInstanceState(savedInstanceState, persistentState)
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration?) {
        baseActivity.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
        baseActivity.onPictureInPictureModeChanged(isInPictureInPictureMode)
    }

    override fun getContentScene(): Scene {
        return baseActivity.getContentScene()
    }

    override fun onCreateThumbnail(outBitmap: Bitmap?, canvas: Canvas?): Boolean {
        return baseActivity.onCreateThumbnail(outBitmap, canvas)
    }

    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
        return baseActivity.onMenuItemSelected(featureId, item)
    }

    override fun invalidateOptionsMenu() {
        baseActivity.invalidateOptionsMenu()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        baseActivity.attachBaseContext(this)
    }

//    override fun getSystemService(name: String): Any? {
//        return baseActivity.getSystemService(name)
//    }

    override fun showLockTaskEscapeMessage() {
        baseActivity.showLockTaskEscapeMessage()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return baseActivity.onKeyUp(keyCode, event)
    }

    override fun getResources(): Resources {
        return baseActivity.resources
    }

    override fun getAssets(): AssetManager {
        return baseActivity.assets
    }

    override fun getClassLoader(): ClassLoader {
        return baseActivity.classLoader
    }
}