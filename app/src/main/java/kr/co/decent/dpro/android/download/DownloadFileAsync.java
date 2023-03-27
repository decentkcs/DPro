package kr.co.decent.dpro.android.download;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
/*
      * Created by Administrator on 2018-04-25.
      * -- 프로그램 업데이트 수신
 */

public class DownloadFileAsync extends AsyncTask<String, String, String> {

	private ProgressDialog mDlg;
	private Context mContext;
	private Boolean bOK;


	private String mFile;


	public DownloadFileAsync(Context context) {
		mContext = context;
	}

	@Override
	protected void onPreExecute() {
		mDlg = new ProgressDialog(mContext);
		mDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mDlg.setMessage("Start");
		mDlg.show();

		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {

		int count = 0;
		bOK = false;
		try {
			Thread.sleep(100);

			Log.d("Download File Async", params[0].toString());

			URL url = new URL(params[0].toString());
			URLConnection conexion = url.openConnection();
			Log.d("Download File Async", "connect");
			conexion.connect();

			Log.d("Download File Async", "connected");
            mFile = Environment.getExternalStorageDirectory().toString() + "/Download/PDA.APK";


			int lenghtOfFile = conexion.getContentLength();

			InputStream input = new BufferedInputStream(url.openStream());

			OutputStream output = new FileOutputStream(mFile);
			Log.d("Download File Async", "file opened");
			byte data[] = new byte[1024];

			long total = 0;
	//		publishProgress("max" + (int) ((total * 100) / lenghtOfFile));
			while ((count = input.read(data)) != -1) {
				total += count;
				String str = String.format("%d",total);
				publishProgress("" + (int) ((total * 100) / lenghtOfFile), str);

				output.write(data, 0, count);
			}

			bOK = true;
			output.flush();
			output.close();
			input.close();
			Log.d("Download File Async","END");



			// �۾��� ����Ǹ鼭 ȣ���ϸ� ȭ���� ���׷��̵带 ����ϰ� �ȴ�
			//publishProgress("progress", 1, "Task " + 1 + " number");
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// ������ ������ �����ϴ� ���� ������ ����� onProgressUpdate �� �Ķ���Ͱ� �ȴ�
		return null;
	}

	@Override
	protected void onProgressUpdate(String... progress) {
		if (progress[0].equals("progress")) {
			mDlg.setProgress(Integer.parseInt(progress[1]));
			mDlg.setMessage(progress[2]);
		} else if (progress[0].equals("max")) {
			mDlg.setMax(Integer.parseInt(progress[1]));
		}
		else{
			mDlg.setProgress(Integer.parseInt(progress[0]));
			mDlg.setMessage(progress[1]);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPostExecute(String unused) {
		mDlg.dismiss();
        if ( bOK == true ) {

			File apkfile = new File(mFile);
			Uri apkUri = Uri.fromFile(apkfile);

			try {

				Intent packageinstaller = new Intent(Intent.ACTION_VIEW);

				packageinstaller.setDataAndType( apkUri, "application/vnd.android.package-archive");

				mContext.startActivity(packageinstaller);

				// Set result and finish this Activity

				Intent intent1 = ((Activity) mContext).getIntent();

				((Activity) mContext).setResult(Activity.RESULT_CANCELED, intent1);
				((Activity) mContext).finish();



			} catch (Exception e) {

			}
		}
		//Toast.makeText(mContext, Integer.toString(result) + " total sum",
				//Toast.LENGTH_SHORT).show();
	}
}
