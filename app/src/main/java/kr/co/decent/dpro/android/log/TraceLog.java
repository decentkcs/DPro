package kr.co.decent.dpro.android.log;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import android.content.Context;
import android.os.Environment;

/**
 * log를 logcat 및 파일로 저장한다.
 * @author hoons
 * @date 2015. 9. 8.
 * @version 1.0.0
 * @Since 1.0.0
 */
public class TraceLog {

	public static final String LOG_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static final String LOG_FILE_PATH2 = "/Download";
	
	public static final int LOG_FILE_MAX_COUNT = 100;
	
	private static boolean isTraceLog = false;
	
	public static void D(String tag, String log) {
		if(!isTraceLog)
			return;
		
		log = tag + " - " + log;
        android.util.Log.d(tag, log);
	}
	
	 public static void I(String tag, String log) {
		if (!isTraceLog) {
			return;
		}

		log = tag + " - " + log;
		android.util.Log.i(tag, log);
	}

	public static void W(String tag, String log) {
        if (!isTraceLog) {
            return;
        }

        log = tag + " - " + log;
        android.util.Log.w(tag, log);
    }
	
    public static void E(String tag, String log) {
        if (!isTraceLog) {
            return;
        }

        log = tag + " - " + log;
        android.util.Log.e(tag, log);
    }

    public static void S(String tag, String log) {
        if (!isTraceLog) {
            return;
        }
        log = tag + " - " + log;
        android.util.Log.d(tag, log);
        writeLog(tag, log);
    }
    
    public static void WW(String tag, String log) {
        if (!isTraceLog) {
            return;
        }
        log = tag + " - " + log;
        android.util.Log.w(tag, log);
        writeLog(tag, log);
    }

    /**
     * 파일에 로그 저장
     * @author hoons
     *
     * @param tag
     * @param log
     */
    private static void writeLog(String tag, String log) {
    	
    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
    	{
    		String path = LOG_FILE_PATH + LOG_FILE_PATH2;
    		String fileName = getCurrentDate("yyyy-MM-dd") + ".txt";
            File file = new File(path , fileName);

            FileWriter fw = null;
            BufferedWriter bw = null;

            try {
                File dir = new File(LOG_FILE_PATH);
                if (dir.exists() == false) {
                    dir.mkdir();

                    dir = new File(path);
                    if (dir.exists() == false) {
                        dir.mkdir();
                    }
                }

                if (file.exists() == false) {
                    file.createNewFile();

                    // kris 26.Aug.13 - 로그 파일 저장 개수는 7개로 제한
                    // After
                    limitLogFileCount(file);
                    //
                }

                fw = new FileWriter(file, true);
                bw = new BufferedWriter(fw);

                final String currentDateAndTime = getCurrentDateAndTime(System.currentTimeMillis());

                bw.newLine();
                bw.append(currentDateAndTime + "    " + log);

                bw.flush();
                bw.close();

            } catch (FileNotFoundException e) {
                System.out.print("예외발생");
            } catch (IOException e) {
                System.out.print("예외발생");
            }
            finally {
                try {
                    if(bw != null)
                        bw.close();
                    if(fw != null)
                        fw.close();
                } catch (IOException e) {
                    System.out.print("예외발생");
                }

            }
        }
    }
    
    
    public static void writeAndroidRuntimeErrorLog(Context context) {
        String[] LOGCAT_CMD = new String[] {
                "logcat",
                "-d",
                "AndroidRuntime:E System.err:* APITask:* Utility:* ReTweetActivity:* ImagePreview:* TextPreview:* MovieView:* *:S" };
        Process logcatProc = null;

        try {
            logcatProc = Runtime.getRuntime().exec(LOGCAT_CMD);
        } catch (IOException e) {
            System.out.print("예외발생");
            return;
        }

        BufferedReader reader = null;
        String lineSeparatoer = System.getProperty("line.separator");
        StringBuilder strOutput = new StringBuilder();

        try {
            reader = new BufferedReader(new InputStreamReader(
                    logcatProc.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                strOutput.append(line);
                strOutput.append(lineSeparatoer);
            }
            reader.close();
        } catch (IOException e) {
            System.out.print("예외발생");
        }

        writeLog("AndroidRuntime:E", strOutput.toString());
    }
    
    
    private static void limitLogFileCount(File file) {
        File mLogFileInExternalMemory = new File(LOG_FILE_PATH + LOG_FILE_PATH2);

        if(mLogFileInExternalMemory == null)
            return;

        int mLogFileCount = mLogFileInExternalMemory.listFiles().length;
        
        if (mLogFileCount >= LOG_FILE_MAX_COUNT) {
            File[] mLogFileListInExternalMemory = mLogFileInExternalMemory
                    .listFiles();

            Arrays.sort(mLogFileListInExternalMemory, new Comparator<File>() {

                @Override
                public int compare(File o1, File o2) {
                    return o2.getName().compareTo(o1.getName());
                }
            });

            FileWriter fw = null;
            BufferedWriter bw = null;
            for (int i = 0; i < mLogFileCount; i++) {
                if (i >= LOG_FILE_MAX_COUNT) {
                    mLogFileListInExternalMemory[i].delete();

                    try {
                        fw = new FileWriter(file, true);
                        bw = new BufferedWriter(fw);

                        String log = "Deleted - Log file name : "
                                + mLogFileListInExternalMemory[i].getName();

                        bw.newLine();
                        bw.append(log);

                        bw.flush();
                        bw.close();
                    } catch (FileNotFoundException e) {
                        System.out.print("예외발생");
                    } catch (IOException e) {
                        System.out.print("예외발생");
                    }
                    finally {
                        try {
                            bw.close();
                            fw.close();
                        } catch (IOException e) {
                            System.out.print("예외발생");
                        }
                    }
                }
            }
        }
    }
    
    public static String getCurrentDate(String format)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		long now = System.currentTimeMillis();
		return dateFormat.format(new Date(now));
	}
    
    public static String getCurrentDateAndTime(long milliseconds)
	{
		Date currentTime = new Date(milliseconds);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(currentTime);
	}
}
