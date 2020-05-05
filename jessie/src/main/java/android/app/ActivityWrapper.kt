package android.app

import android.annotation.SuppressLint
import android.app.assist.AssistContent
import android.content.*
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

@SuppressLint("MissingPermission", "NewApi")
abstract class ActivityWrapper : Activity() {
    abstract val base: Activity

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return base.onKeyDown(keyCode, event)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return base.onContextItemSelected(item)
    }

    override fun setExitSharedElementCallback(callback: SharedElementCallback?) {
        base.setExitSharedElementCallback(callback)
    }

    override fun onAttachedToWindow() {
        base.onAttachedToWindow()
    }

    override fun startNextMatchingActivity(intent: Intent): Boolean {
        return base.startNextMatchingActivity(intent)
    }

    override fun startNextMatchingActivity(intent: Intent, options: Bundle?): Boolean {
        return base.startNextMatchingActivity(intent, options)
    }

    override fun <T : View?> findViewById(id: Int): T {
        return base.findViewById<T>(id)
    }

    override fun startIntentSenderForResult(intent: IntentSender?, requestCode: Int, fillInIntent: Intent?, flagsMask: Int, flagsValues: Int, extraFlags: Int) {
        base.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags)
    }

    override fun startIntentSenderForResult(intent: IntentSender?, requestCode: Int, fillInIntent: Intent?, flagsMask: Int, flagsValues: Int, extraFlags: Int, options: Bundle?) {
        base.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options)
    }

    override fun hasWindowFocus(): Boolean {
        return base.hasWindowFocus()
    }

    override fun setPictureInPictureParams(params: PictureInPictureParams) {
        base.setPictureInPictureParams(params)
    }

    override fun isImmersive(): Boolean {
        return base.isImmersive()
    }

    override fun isVoiceInteraction(): Boolean {
        return base.isVoiceInteraction()
    }

    override fun onProvideAssistData(data: Bundle?) {
        base.onProvideAssistData(data)
    }

    override fun registerActivityLifecycleCallbacks(callback: Application.ActivityLifecycleCallbacks) {
        base.registerActivityLifecycleCallbacks(callback)
    }

    override fun onPause() {
        super.onPause()
        base.onPause()
    }

    override fun getMenuInflater(): MenuInflater {
        return base.getMenuInflater()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return base.onTouchEvent(event)
    }

    override fun takeKeyEvents(get: Boolean) {
        base.takeKeyEvents(get)
    }

    override fun onPreparePanel(featureId: Int, view: View?, menu: Menu): Boolean {
        return base.onPreparePanel(featureId, view, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return base.onPrepareOptionsMenu(menu)
    }

    override fun setEnterSharedElementCallback(callback: SharedElementCallback?) {
        base.setEnterSharedElementCallback(callback)
    }

    override fun startActivityFromFragment(fragment: Fragment, intent: Intent?, requestCode: Int) {
        base.startActivityFromFragment(fragment, intent, requestCode)
    }

    override fun startActivityFromFragment(fragment: Fragment, intent: Intent?, requestCode: Int, options: Bundle?) {
        base.startActivityFromFragment(fragment, intent, requestCode, options)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        base.onActivityResult(requestCode, resultCode, data)
    }

    override fun onActionModeFinished(mode: ActionMode?) {
        super.onActionModeFinished(mode)
        base.onActionModeFinished(mode)
    }

    override fun getLayoutInflater(): LayoutInflater {
        return base.getLayoutInflater()
    }

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean, newConfig: Configuration?) {
        base.onMultiWindowModeChanged(isInMultiWindowMode, newConfig)
    }

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean) {
        base.onMultiWindowModeChanged(isInMultiWindowMode)
    }

    override fun getLastNonConfigurationInstance(): Any? {
        return base.getLastNonConfigurationInstance()
    }

    override fun onEnterAnimationComplete() {
        base.onEnterAnimationComplete()
    }

    override fun onKeyShortcut(keyCode: Int, event: KeyEvent?): Boolean {
        return base.onKeyShortcut(keyCode, event)
    }

    override fun setInheritShowWhenLocked(inheritShowWhenLocked: Boolean) {
        base.setInheritShowWhenLocked(inheritShowWhenLocked)
    }

    override fun dispatchKeyShortcutEvent(event: KeyEvent?): Boolean {
        return base.dispatchKeyShortcutEvent(event)
    }

    override fun unregisterActivityLifecycleCallbacks(callback: Application.ActivityLifecycleCallbacks) {
        base.unregisterActivityLifecycleCallbacks(callback)
    }

    override fun setShowWhenLocked(showWhenLocked: Boolean) {
        base.setShowWhenLocked(showWhenLocked)
    }

    override fun onCreateNavigateUpTaskStack(builder: TaskStackBuilder?) {
        base.onCreateNavigateUpTaskStack(builder)
    }

    override fun registerForContextMenu(view: View?) {
        base.registerForContextMenu(view)
    }

    override fun getTaskId(): Int {
        return base.getTaskId()
    }

    override fun onContextMenuClosed(menu: Menu) {
        base.onContextMenuClosed(menu)
    }

    override fun getCallingActivity(): ComponentName? {
        return base.getCallingActivity()
    }

    override fun onNavigateUp(): Boolean {
        return base.onNavigateUp()
    }

    override fun postponeEnterTransition() {
        base.postponeEnterTransition()
    }

    override fun onPerformDirectAction(actionId: String, arguments: Bundle, cancellationSignal: CancellationSignal, resultListener: Consumer<Bundle>) {
        base.onPerformDirectAction(actionId, arguments, cancellationSignal, resultListener)
    }

    override fun onCreateDescription(): CharSequence? {
        return base.onCreateDescription()
    }

    override fun isTaskRoot(): Boolean {
        return base.isTaskRoot()
    }

    override fun onPostResume() {
        super.onPostResume()
        base.onPostResume()
    }

    override fun stopManagingCursor(c: Cursor?) {
        base.stopManagingCursor(c)
    }

    override fun getMaxNumPictureInPictureActions(): Int {
        return base.getMaxNumPictureInPictureActions()
    }

    override fun addContentView(view: View?, params: ViewGroup.LayoutParams?) {
        base.addContentView(view, params)
    }

    override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return base.shouldShowRequestPermissionRationale(permission)
    }

    override fun triggerSearch(query: String?, appSearchData: Bundle?) {
        base.triggerSearch(query, appSearchData)
    }

    override fun setTaskDescription(taskDescription: ActivityManager.TaskDescription?) {
        base.setTaskDescription(taskDescription)
    }

    override fun isLocalVoiceInteractionSupported(): Boolean {
        return base.isLocalVoiceInteractionSupported()
    }

    override fun onTrimMemory(level: Int) {
        base.onTrimMemory(level)
    }

    override fun startLockTask() {
        base.startLockTask()
    }

    override fun setTurnScreenOn(turnScreenOn: Boolean) {
        base.setTurnScreenOn(turnScreenOn)
    }

    override fun onProvideKeyboardShortcuts(data: MutableList<KeyboardShortcutGroup>?, menu: Menu?, deviceId: Int) {
        base.onProvideKeyboardShortcuts(data, menu, deviceId)
    }

    override fun setContentTransitionManager(tm: TransitionManager?) {
        base.setContentTransitionManager(tm)
    }

    override fun onAttachFragment(fragment: Fragment?) {
        base.onAttachFragment(fragment)
    }

    override fun setVisible(visible: Boolean) {
        base.setVisible(visible)
    }

    override fun isChangingConfigurations(): Boolean {
        return base.isChangingConfigurations()
    }

    override fun finishActivity(requestCode: Int) {
        base.finishActivity(requestCode)
    }

    override fun onActionModeStarted(mode: ActionMode?) {
        super.onActionModeStarted(mode)
        base.onActionModeStarted(mode)
    }

    override fun isInMultiWindowMode(): Boolean {
        return base.isInMultiWindowMode()
    }

    override fun onTopResumedActivityChanged(isTopResumedActivity: Boolean) {
        base.onTopResumedActivityChanged(isTopResumedActivity)
    }

    override fun setTitleColor(textColor: Int) {
        base.setTitleColor(textColor)
    }

    override fun startActionMode(callback: ActionMode.Callback?): ActionMode? {
        return base.startActionMode(callback)
    }

    override fun startActionMode(callback: ActionMode.Callback?, type: Int): ActionMode? {
        return base.startActionMode(callback, type)
    }

    override fun setContentView(layoutResID: Int) {
        base.setContentView(layoutResID)
    }

    override fun setContentView(view: View?) {
        base.setContentView(view)
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        base.setContentView(view, params)
    }

    override fun stopLockTask() {
        base.stopLockTask()
    }

    override fun onUserInteraction() {
        base.onUserInteraction()
    }

    override fun setImmersive(i: Boolean) {
        base.setImmersive(i)
    }

    override fun getChangingConfigurations(): Int {
        return base.getChangingConfigurations()
    }

    override fun onKeyMultiple(keyCode: Int, repeatCount: Int, event: KeyEvent?): Boolean {
        return base.onKeyMultiple(keyCode, repeatCount, event)
    }

    override fun shouldUpRecreateTask(targetIntent: Intent?): Boolean {
        return base.shouldUpRecreateTask(targetIntent)
    }

    override fun navigateUpTo(upIntent: Intent?): Boolean {
        return base.navigateUpTo(upIntent)
    }

    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        return base.onMenuOpened(featureId, menu)
    }

    override fun onPanelClosed(featureId: Int, menu: Menu) {
        base.onPanelClosed(featureId, menu)
    }

    override fun isInPictureInPictureMode(): Boolean {
        return base.isInPictureInPictureMode()
    }

    override fun finish() {
        base.finish()
    }

    override fun finishAffinity() {
        base.finishAffinity()
    }

    override fun onCreateDialog(id: Int): Dialog {
        return base.onCreateDialog(id)
    }

    override fun onCreateDialog(id: Int, args: Bundle?): Dialog? {
        return base.onCreateDialog(id, args)
    }

    override fun startActivityFromChild(child: Activity, intent: Intent?, requestCode: Int) {
        base.startActivityFromChild(child, intent, requestCode)
    }

    override fun startActivityFromChild(child: Activity, intent: Intent?, requestCode: Int, options: Bundle?) {
        base.startActivityFromChild(child, intent, requestCode, options)
    }

    override fun enterPictureInPictureMode() {
        base.enterPictureInPictureMode()
    }

    override fun enterPictureInPictureMode(params: PictureInPictureParams): Boolean {
        return base.enterPictureInPictureMode(params)
    }

    override fun finishFromChild(child: Activity?) {
        base.finishFromChild(child)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        base.onWindowFocusChanged(hasFocus)
    }

    override fun moveTaskToBack(nonRoot: Boolean): Boolean {
        return base.moveTaskToBack(nonRoot)
    }

    override fun finishActivityFromChild(child: Activity, requestCode: Int) {
        base.finishActivityFromChild(child, requestCode)
    }

    override fun onDestroy() {
        super.onDestroy()
        base.onDestroy()
    }

    override fun getActionBar(): ActionBar? {
        return base.getActionBar()
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        base.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun getContentTransitionManager(): TransitionManager {
        return base.getContentTransitionManager()
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        base.onActivityReenter(resultCode, data)
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        return base.onKeyLongPress(keyCode, event)
    }

    override fun onBackPressed() {
        base.onBackPressed()
    }

    override fun onLocalVoiceInteractionStopped() {
        base.onLocalVoiceInteractionStopped()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        base.onConfigurationChanged(newConfig)
    }

    override fun navigateUpToFromChild(child: Activity?, upIntent: Intent?): Boolean {
        return base.navigateUpToFromChild(child, upIntent)
    }

    override fun setActionBar(toolbar: Toolbar?) {
        base.setActionBar(toolbar)
    }

    override fun onVisibleBehindCanceled() {
        super.onVisibleBehindCanceled()
        base.onVisibleBehindCanceled()
    }

    override fun openContextMenu(view: View?) {
        base.openContextMenu(view)
    }

    override fun getParentActivityIntent(): Intent? {
        return base.getParentActivityIntent()
    }

    override fun startIntentSender(intent: IntentSender?, fillInIntent: Intent?, flagsMask: Int, flagsValues: Int, extraFlags: Int) {
        base.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags)
    }

    override fun startIntentSender(intent: IntentSender?, fillInIntent: Intent?, flagsMask: Int, flagsValues: Int, extraFlags: Int, options: Bundle?) {
        base.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags, options)
    }

    override fun closeContextMenu() {
        base.closeContextMenu()
    }

    override fun onPrepareDialog(id: Int, dialog: Dialog?) {
        base.onPrepareDialog(id, dialog)
    }

    override fun onPrepareDialog(id: Int, dialog: Dialog?, args: Bundle?) {
        base.onPrepareDialog(id, dialog, args)
    }

    override fun onNewIntent(intent: Intent?) {
        base.onNewIntent(intent)
    }

    override fun getRequestedOrientation(): Int {
        return base.getRequestedOrientation()
    }

    override fun getVoiceInteractor(): VoiceInteractor {
        return base.getVoiceInteractor()
    }

    override fun dump(prefix: String, fd: FileDescriptor?, writer: PrintWriter, args: Array<String>?) {
        base.dump(prefix, fd, writer, args)
    }

    override fun onContentChanged() {
        base.onContentChanged()
    }

    override fun releaseInstance(): Boolean {
        return base.releaseInstance()
    }

    override fun onSearchRequested(searchEvent: SearchEvent?): Boolean {
        return base.onSearchRequested(searchEvent)
    }

    override fun onSearchRequested(): Boolean {
        return base.onSearchRequested()
    }

    override fun onNavigateUpFromChild(child: Activity?): Boolean {
        return base.onNavigateUpFromChild(child)
    }

    override fun getReferrer(): Uri? {
        return base.getReferrer()
    }

    override fun startLocalVoiceInteraction(privateOptions: Bundle?) {
        base.startLocalVoiceInteraction(privateOptions)
    }

    override fun setRequestedOrientation(requestedOrientation: Int) {
        base.setRequestedOrientation(requestedOrientation)
    }

    override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent?): Boolean {
        return base.dispatchPopulateAccessibilityEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ReflectionUtils.copyFields(Activity::class.java, this, base)
        base.onCreate(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        base.onCreate(savedInstanceState, persistentState)
    }

    override fun onLowMemory() {
        base.onLowMemory()
    }

    override fun reportFullyDrawn() {
        base.reportFullyDrawn()
    }

    override fun onRetainNonConfigurationInstance(): Any {
        return base.onRetainNonConfigurationInstance()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return base.onCreateOptionsMenu(menu)
    }

    override fun startPostponedEnterTransition() {
        base.startPostponedEnterTransition()
    }

    override fun getLoaderManager(): LoaderManager {
        return base.getLoaderManager()
    }

    override fun isActivityTransitionRunning(): Boolean {
        return base.isActivityTransitionRunning()
    }

    override fun onStart() {
        super.onStart()
        base.onStart()
    }

    override fun unregisterForContextMenu(view: View?) {
        base.unregisterForContextMenu(view)
    }

    override fun overridePendingTransition(enterAnim: Int, exitAnim: Int) {
        base.overridePendingTransition(enterAnim, exitAnim)
    }

    override fun onGenericMotionEvent(event: MotionEvent?): Boolean {
        return base.onGenericMotionEvent(event)
    }

    override fun onProvideAssistContent(outContent: AssistContent?) {
        base.onProvideAssistContent(outContent)
    }

    override fun stopLocalVoiceInteraction() {
        base.stopLocalVoiceInteraction()
    }

    override fun recreate() {
        base.recreate()
    }

    override fun onOptionsMenuClosed(menu: Menu?) {
        base.onOptionsMenuClosed(menu)
    }

    override fun createPendingResult(requestCode: Int, data: Intent, flags: Int): PendingIntent {
        return base.createPendingResult(requestCode, data, flags)
    }

    override fun onWindowAttributesChanged(params: WindowManager.LayoutParams?) {
        base.onWindowAttributesChanged(params)
    }

    override fun startActivities(intents: Array<out Intent>?) {
        base.startActivities(intents)
    }

    override fun startActivities(intents: Array<out Intent>?, options: Bundle?) {
        base.startActivities(intents, options)
    }

    override fun onUserLeaveHint() {
        base.onUserLeaveHint()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        base.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun startManagingCursor(c: Cursor?) {
        base.startManagingCursor(c)
    }

    override fun onResume() {
        super.onResume()
        base.onResume()
    }

    override fun onTitleChanged(title: CharSequence?, color: Int) {
        base.onTitleChanged(title, color)
    }

    override fun onProvideReferrer(): Uri {
        return base.onProvideReferrer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        base.onSaveInstanceState(outState)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        base.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onChildTitleChanged(childActivity: Activity?, title: CharSequence?) {
        base.onChildTitleChanged(childActivity, title)
    }

    override fun onStateNotSaved() {
        base.onStateNotSaved()
    }

    override fun dispatchGenericMotionEvent(ev: MotionEvent?): Boolean {
        return base.dispatchGenericMotionEvent(ev)
    }

    override fun openOptionsMenu() {
        base.openOptionsMenu()
    }

    override fun dispatchTrackballEvent(ev: MotionEvent?): Boolean {
        return base.dispatchTrackballEvent(ev)
    }

    override fun setTitle(title: CharSequence?) {
        base.setTitle(title)
    }

    override fun setTitle(titleId: Int) {
        base.setTitle(titleId)
    }

    override fun isVoiceInteractionRoot(): Boolean {
        return base.isVoiceInteractionRoot()
    }

    override fun startActivity(intent: Intent?) {
        base.startActivity(intent)
    }

    override fun startActivity(intent: Intent?, options: Bundle?) {
        base.startActivity(intent, options)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return base.onCreateView(name, context, attrs)
    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        return base.onCreateView(parent, name, context, attrs)
    }

    override fun requestDragAndDropPermissions(event: DragEvent?): DragAndDropPermissions {
        return base.requestDragAndDropPermissions(event)
    }

    override fun isDestroyed(): Boolean {
        return base.isDestroyed()
    }

    override fun setVrModeEnabled(enabled: Boolean, requestedComponent: ComponentName) {
        base.setVrModeEnabled(enabled, requestedComponent)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        base.onPostCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        base.onPostCreate(savedInstanceState, persistentState)
    }

    override fun onApplyThemeResource(theme: Resources.Theme?, resid: Int, first: Boolean) {
        base.onApplyThemeResource(theme, resid, first)
    }

    override fun startIntentSenderFromChild(child: Activity?, intent: IntentSender?, requestCode: Int, fillInIntent: Intent?, flagsMask: Int, flagsValues: Int, extraFlags: Int) {
        base.startIntentSenderFromChild(child, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags)
    }

    override fun startIntentSenderFromChild(child: Activity?, intent: IntentSender?, requestCode: Int, fillInIntent: Intent?, flagsMask: Int, flagsValues: Int, extraFlags: Int, options: Bundle?) {
        base.startIntentSenderFromChild(child, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options)
    }

    override fun onLocalVoiceInteractionStarted() {
        base.onLocalVoiceInteractionStarted()
    }

    override fun onGetDirectActions(cancellationSignal: CancellationSignal, callback: Consumer<MutableList<DirectAction>>) {
        base.onGetDirectActions(cancellationSignal, callback)
    }

    override fun onTrackballEvent(event: MotionEvent?): Boolean {
        return base.onTrackballEvent(event)
    }

    override fun onDetachedFromWindow() {
        base.onDetachedFromWindow()
    }

    override fun closeOptionsMenu() {
        base.closeOptionsMenu()
    }

    override fun finishAndRemoveTask() {
        base.finishAndRemoveTask()
    }

    override fun requestVisibleBehind(visible: Boolean): Boolean {
        return base.requestVisibleBehind(visible)
    }

    override fun onCreatePanelView(featureId: Int): View? {
        return base.onCreatePanelView(featureId)
    }

    override fun onPrepareNavigateUpTaskStack(builder: TaskStackBuilder?) {
        base.onPrepareNavigateUpTaskStack(builder)
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        base.startActivityForResult(intent, requestCode)
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        base.startActivityForResult(intent, requestCode, options)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return base.dispatchTouchEvent(ev)
    }

    override fun onCreatePanelMenu(featureId: Int, menu: Menu): Boolean {
        return base.onCreatePanelMenu(featureId, menu)
    }

    override fun onWindowStartingActionMode(callback: ActionMode.Callback?): ActionMode? {
        return base.onWindowStartingActionMode(callback)
    }

    override fun onWindowStartingActionMode(callback: ActionMode.Callback?, type: Int): ActionMode? {
        return base.onWindowStartingActionMode(callback, type)
    }

    override fun getLocalClassName(): String {
        return base.getLocalClassName()
    }

    override fun getPreferences(mode: Int): SharedPreferences {
        return base.getPreferences(mode)
    }

    override fun onStop() {
        super.onStop()
        base.onStop()
    }

    override fun getCurrentFocus(): View? {
        return base.getCurrentFocus()
    }

    override fun onRestart() {
        super.onRestart()
        base.onRestart()
    }

    override fun startActivityIfNeeded(intent: Intent, requestCode: Int): Boolean {
        return base.startActivityIfNeeded(intent, requestCode)
    }

    override fun startActivityIfNeeded(intent: Intent, requestCode: Int, options: Bundle?): Boolean {
        return base.startActivityIfNeeded(intent, requestCode, options)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return base.onOptionsItemSelected(item)
    }

    override fun setIntent(newIntent: Intent?) {
        base.setIntent(newIntent)
    }

    override fun getFragmentManager(): FragmentManager {
        return base.getFragmentManager()
    }

    override fun getCallingPackage(): String? {
        return base.getCallingPackage()
    }

    override fun showAssist(args: Bundle?): Boolean {
        return base.showAssist(args)
    }

    override fun startSearch(initialQuery: String?, selectInitialQuery: Boolean, appSearchData: Bundle?, globalSearch: Boolean) {
        base.startSearch(initialQuery, selectInitialQuery, appSearchData, globalSearch)
    }

    override fun finishAfterTransition() {
        base.finishAfterTransition()
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        return base.dispatchKeyEvent(event)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        base.onRestoreInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        base.onRestoreInstanceState(savedInstanceState, persistentState)
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration?) {
        base.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
        base.onPictureInPictureModeChanged(isInPictureInPictureMode)
    }

    override fun getContentScene(): Scene {
        return base.getContentScene()
    }

    override fun onCreateThumbnail(outBitmap: Bitmap?, canvas: Canvas?): Boolean {
        return base.onCreateThumbnail(outBitmap, canvas)
    }

    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
        return base.onMenuItemSelected(featureId, item)
    }

    override fun invalidateOptionsMenu() {
        base.invalidateOptionsMenu()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        base.attachBaseContext(newBase)
    }

    override fun getSystemService(name: String): Any? {
        return base.getSystemService(name)
    }

    override fun showLockTaskEscapeMessage() {
        base.showLockTaskEscapeMessage()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return base.onKeyUp(keyCode, event)
    }

    override fun setFinishOnTouchOutside(finish: Boolean) {
        base.setFinishOnTouchOutside(finish)
    }
}