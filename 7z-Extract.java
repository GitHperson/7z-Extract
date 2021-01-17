package cn.gitp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.internal.compiler.apt.dispatch.RoundDispatcher;
import org.omg.CORBA.PRIVATE_MEMBER;

import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;

//用于暴力破解7z、rar、zip等密码
public class z7 {
	//解压密码核对模块，file7zPath压缩文件路径，outPutPath解压文件存放路径，passWord压缩密码
	public static boolean un7z(String file7zPath, final String outPutPath, String passWord) throws Exception {
        boolean flag = false;
		IInArchive archive;
        RandomAccessFile randomAccessFile;
        randomAccessFile = new RandomAccessFile(file7zPath, "r");
        archive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile), passWord);
        int numberOfItems = archive.getNumberOfItems();
        ISimpleInArchive simpleInArchive = archive.getSimpleInterface();
        //for (final ISimpleInArchiveItem it\em : simpleInArchive.getArchiveItems()) {
        ISimpleInArchiveItem item=simpleInArchive.getArchiveItems()[1];
        	if (!item.isFolder()) {
                ExtractOperationResult result;
                result = item.extractSlow(new ISequentialOutStream() {
                    public int write(byte[] data) throws SevenZipException {
                        try {
                            String parentFilePath = outPutPath;
                            if (!new File(parentFilePath).exists()) {
                                new File(parentFilePath).mkdirs();
                            }
                            IOUtils.write(data, new FileOutputStream(new File(outPutPath + File.separator + item.getPath()), true));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return data.length; // Return amount of consumed
                    }
                }, passWord);
                if (result == ExtractOperationResult.OK) {
                    flag=true;
                } else {
                    flag=false;
                }
            }
        archive.close();
        randomAccessFile.close();
        return flag;
    }

    public static void main(String[] args) throws Exception {
    	//密码有几位就有几个变量
    	byte a7,a6,a5,a4,a3,a2,a1,a0;
    	//临时生成的密码
    	StringBuffer sb;
    	//每一位密码都可能是键盘所有字符之一，a7=32;a7<127代表ascii所有可打印字符
    	//密码是几位就设几层for循环
    	for(a7=32;a7<127;a7++)
        for(a6=32;a6<127;a6++)
        for(a5=32;a5<127;a5++)
        for(a4=32;a4<127;a4++)
        for(a3=32;a3<127;a3++)
        for(a2=32;a2<127;a2++)
        for(a1=32;a1<127;a1++)
        for(a0=32;a0<127;a0++)
        {
        	//拼接密码
        	sb=new StringBuffer().append((char)a0).append((char)a1).append((char)a2).append((char)a3).append((char)a4).append((char)a5).append((char)a6).append((char)a7);
        	if(un7z("D:\\1.7z", "D:\\1", sb.toString())){
    			System.out.println("密码是"+sb.toString());
    			return;
    			}
    		System.out.println("解压失败"+sb.toString());
    		}
        }
}
