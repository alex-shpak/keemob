## Cordova wrapper for [Keeweb](https://keeweb.info/)
WARNING: Under early development

![keemob](https://cloud.githubusercontent.com/assets/5515443/26451042/9bbb9fb6-415a-11e7-9898-d5ef2fcd5068.gif)

## Build
> $ cordova build android --release  
> $ zipalign -v -p 4 app-release-unsigned.apk app-release-unsigned-aligned.apk  
> $ apksigner sign --ks ~/keemob.jks --out keemob-0.0.X.apk app-release-unsigned-aligned.apk  

## Licence
[MIT](LICENCE.txt)