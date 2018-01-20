package com.api.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import org.apache.commons.io.FileUtils;


/**
 * Provides working with all used application files.
 */
public class FileSystem
{
	private static final String tempCaptchaPath = "/cache/temp_captcha/";



	public static File downloadCaptchaToFile(String captchaURL) throws IOException
	{
		File captchaImageFile = new File (tempCaptchaPath+"/"+UUID.nameUUIDFromBytes(captchaURL.getBytes()).toString());
		
	//	captchaImageFile.getParentFile().mkdirs();
	//	captchaImageFile.createNewFile();
		
		FileUtils.copyURLToFile(new URL(captchaURL), captchaImageFile);
		
		return captchaImageFile;
	}
	

}
