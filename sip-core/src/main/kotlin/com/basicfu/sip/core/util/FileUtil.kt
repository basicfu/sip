package com.basicfu.sip.core.util

import java.io.*
import java.io.File


/**
 * @author basicfu
 * @date 2018/7/13
 */
object FileUtil {
    fun exists(path: String): Boolean {
        return File(path).exists()
    }

    fun copyFile(f1: File, f2: File) {
        var length: Int
        try {
            val `in` = FileInputStream(f1)
            val out = FileOutputStream(f2)
            val inC = `in`.channel
            val outC = out.channel
            while (true) {
                if (inC.position() == inC.size()) {
                    inC.close()
                    outC.close()
                    break
                }
                length = if (inC.size() - inC.position() < 20971520) {
                    (inC.size() - inC.position()).toInt()
                } else {
                    20971520
                }
                inC.transferTo(inC.position(), length.toLong(), outC)
                inC.position(inC.position() + length)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun pathToByte(path: String?): ByteArray? {
        if (path == null) {
            return null
        }
        val out: ByteArrayOutputStream
        val `in`: BufferedInputStream
        try {
            val file = File(path)
            `in` = BufferedInputStream(FileInputStream(file))
            out = ByteArrayOutputStream(file.length().toInt())
            //读取文件内容
            val buffer = ByteArray(1024)
            var len = `in`.read(buffer, 0, buffer.size) //读取长度
            while (len != -1) {
                out.write(buffer, 0, len)
                len = `in`.read(buffer, 0, buffer.size)
            }
            out.flush() //刷新缓冲的输出流
            `in`.close()
            out.close()
            return out.toByteArray()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun fileToByte(file: File): ByteArray? {
        var buffer: ByteArray? = null
        try {
            val fis = FileInputStream(file)
            val bos = ByteArrayOutputStream()
            val b = ByteArray(1024)
            var n = fis.read(b)
            while (n != -1) {
                bos.write(b, 0, n)
                n = fis.read(b)
            }
            fis.close()
            bos.close()
            buffer = bos.toByteArray()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return buffer
    }

    fun byteToFile(buf: ByteArray, filePath: String, fileName: String) {
        var bos: BufferedOutputStream? = null
        var fos: FileOutputStream? = null
        val file: File?
        try {
            val dir = File(filePath)
            if (!dir.exists() && dir.isDirectory) {
                dir.mkdirs()
            }
            file = File(filePath + File.separator + fileName)
            fos = FileOutputStream(file)
            bos = BufferedOutputStream(fos)
            bos.write(buf)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (bos != null) {
                try {
                    bos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun inputToByte(inStream: InputStream): ByteArray {
        val swapStream = ByteArrayOutputStream()
        val buff = ByteArray(1024)
        var rc = inStream.read(buff, 0, 100)
        while (rc > 0) {
            swapStream.write(buff, 0, rc)
            rc = inStream.read(buff, 0, 100)
        }
        val in2b = swapStream.toByteArray()
        return in2b
    }

    fun readTxtFileEnter(filePath: String): String {
        val sb = StringBuffer()
        try {
            val file = File(filePath)
            if (file.isFile && file.exists()) { //判断文件是否存在
                val read = InputStreamReader(FileInputStream(file), "UTF-8")//考虑到编码格式
                val bufferedReader = BufferedReader(read)
                var lineTxt = bufferedReader.readLine()
                while (lineTxt != null) {
                    sb.append(lineTxt + "\n")
                    lineTxt = bufferedReader.readLine()
                }
                read.close()
            } else {
                return ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sb.toString()
    }

    fun readTxtFile(filePath: String): String {
        val sb = StringBuffer()
        try {
            val file = File(filePath)
            if (file.isFile && file.exists()) { //判断文件是否存在
                val read = InputStreamReader(FileInputStream(file), "UTF-8")//考虑到编码格式
                val bufferedReader = BufferedReader(read)
                var lineTxt = bufferedReader.readLine()
                while (lineTxt != null) {
                    sb.append(lineTxt)
                    lineTxt = bufferedReader.readLine()
                }
                read.close()
            } else {
                return ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sb.toString()
    }

    fun writeTxtFile(text: String, file: String) {
        val mm: RandomAccessFile? = null
        val o: FileOutputStream?
        try {
            o = FileOutputStream(file)
            o.write(text.toByteArray(charset("GBK")))
            o.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mm?.close()
        }
    }

    fun appendTextFile(text: String, file: String) {
        try {
            val randomFile = RandomAccessFile(file, "rw")
            randomFile.seek(randomFile.length())
            randomFile.writeBytes(text + "\r\n")
            randomFile.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
//    fun appendTextFile(){
//        var fw: FileWriter? = null
//        try {
//            val f = File("E:\\dd.txt")
//            fw = FileWriter(f, true)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        val pw = PrintWriter(fw!!)
//        pw.println("追加内容")
//        pw.flush()
//        try {
//            fw.flush()
//            pw.close()
//            fw.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
