# retain these to support class references for the bundling and translation systems
-keepnames class com.shatteredpixel.** { *; }
-keepnames class com.watabou.** { *; }

# keep classes that are instantiated via reflection
-keep class * extends com.watabou.noosa.Gizmo { *; }
-keep class * extends com.watabou.glscripts.Script { *; }
-keep class * implements com.watabou.utils.Bundlable { *; }

# TrialChamber (VeiledSanctumLevel's trial rooms) is instantiated via Reflection.newInstance()
# from a Class<?>[] array, but it isn't Bundlable/Gizmo/Script, so it isn't covered by the rules
# above. Without this, R8 can strip its no-arg constructors in release builds, silently breaking
# Reflection.newInstance() (it swallows the exception and returns null) and leaving every trial
# room in the level empty.
-keep class com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers.** { *; }

# retained to support meaningful stack traces
# note that the mapping file must be referenced in order to make sense of line numbers
# mapping file can be found in core/build/outputs/mapping after running a release build
-keepattributes SourceFile,LineNumberTable

# libGDX stuff
-dontwarn android.support.**
-dontwarn com.badlogic.gdx.backends.android.AndroidFragmentApplication
-dontwarn com.badlogic.gdx.utils.GdxBuild
-dontwarn com.badlogic.gdx.physics.box2d.utils.Box2DBuild
-dontwarn com.badlogic.gdx.jnigen.BuildTarget*

# needed for libGDX skin reflection used in text fields. Perhaps just don't use skin?
-keep class com.badlogic.gdx.graphics.Color { *; }
-keep class com.badlogic.gdx.scenes.scene2d.ui.TextField$TextFieldStyle { *; }
-keepnames class com.badlogic.gdx.scenes.scene2d.ui.TextField { *; }

# needed for libGDX controllers
-keep class com.badlogic.gdx.controllers.android.AndroidControllers { *; }

-keepclassmembers class com.badlogic.gdx.backends.android.AndroidInput* {
    <init>(com.badlogic.gdx.Application, android.content.Context, java.lang.Object, com.badlogic.gdx.backends.android.AndroidApplicationConfiguration);
}