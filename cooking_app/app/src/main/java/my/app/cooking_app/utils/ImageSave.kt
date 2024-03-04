package my.app.cooking_app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class ImageSave {
    companion object{
        val imageSaveLink = "/data/user/0/my.app.cooking_app/files/"
        fun saveBitmapToInternalStorage(context: Context, bitmap: Bitmap, fileName: String): String? {
            val fileOutputStream: OutputStream
            var filePath: String? = null
            try {
                // 내부 저장소 디렉토리 가져오기
                val directory = context.filesDir
                // 파일 경로 생성
                val file = File(directory, fileName)
                // 파일 출력 스트림 열기
                fileOutputStream = FileOutputStream(file)
                // 비트맵을 파일에 저장
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, fileOutputStream)
                // 파일 경로 가져오기
                filePath = file.absolutePath
                // 파일 닫기
                fileOutputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return filePath
        }
        fun loadBitmapFromFilePath(filePath: String): Bitmap? {
            return try {
                val options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.ARGB_8888
                BitmapFactory.decodeFile(imageSaveLink+filePath, options)
            } catch (e: Exception) {
                Log.d(
                    "error in loadBitmapFromFilePath", e.printStackTrace()
                        .toString()
                )
                null
            }
        }
        fun deletePngFilesInDirectory() {
            val directory = File(imageSaveLink)

            if (directory.exists() && directory.isDirectory) {
                val files = directory.listFiles()

                if (files != null) {
                    for (file in files) {
                        if (file.isFile && file.name.endsWith(".jpg")) {
                            file.delete()
                        }
                    }
                } else {
                    println("No files to delete in the directory.")
                }
            } else {
                println("Invalid directory path.")
            }
        }
    }

}