# Keep models used by Kotlinx Serialization
-keepclassmembers class **$$serializer { *; }
-keepclassmembers class ** implements kotlinx.serialization.KSerializer { *; }
-keepattributes *Annotation*
