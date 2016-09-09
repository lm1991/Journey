package com.mesor.journey.addmark;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mesor.journey.R;
import com.mesor.journey.addmark.model.InfoDir;
import com.mesor.journey.framework.BaseFragment;
import com.mesor.journey.framework.SharedFragmentActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ImagePickFragment extends BaseFragment {

	protected static final int REQUEST_CODE_CAPTRUE_IMAGE = 1;
	protected static final int CHANGE_UI = 0;
	@BindView(R.id.imgContentGridV)
	GridView imgContentGridV;

	@BindView(R.id.dirNameTv)
	TextView dirNameTv;
	@BindView(R.id.chooseCountTv)
	TextView chooseCountTv;

	/**
	 * 拍照返回图片uri
	 */
	Uri tempImgUri;

	/**
	 * 所有图片列表
	 */
	private List<String> imgUriList;

	private List<String> showUriList;

	private String currentDirString;

	/**
	 * 所有图片所在文件夹列表， key为文件夹地址
	 */
	private List<InfoDir> dirMap;

	private ImagePickAdapter adapter;
	int num = 0;

	/**
	 * 最多选择数， 单选多选
	 */
	private int maxChoosedCount = 9;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CHANGE_UI:
				num = msg.arg1;
				chooseCountTv.setText(getString(R.string.photo_count, num));
				titleLayout.setRightText("完成" + "(" + num + "/" + maxChoosedCount + ")");
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		maxChoosedCount = getArguments().getInt("max_count", 1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_pick_image);
	}

	@Override
	public void initView() {
		chooseCountTv.setText(getString(R.string.photo_count, num));
		titleLayout.setRightText("完成" + "(" + num + "/" + maxChoosedCount + ")");
		setOnTitleListener(new TitleListener() {

			@Override
			public void onClickRight() {
				if (adapter.getSelectList().size() == 0)
					return;
				Intent intent = new Intent();
				intent.putExtra("img_list", adapter.getSelectList());
				getActivity().setResult(Activity.RESULT_OK, intent);
				getActivity().finish();
			}
			@Override
			public void onClickBack() {
				getActivity().finish();
			}
		});
		// imgContentGridV.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int
		// position, long id) {
		// if (position == 0) {
		// photograph();
		// } else {
		// File file = new File(Constants.BASE_DIR + "cache/temp.png");
		// Utils.copyFile(showUriList.get(position - 1), file.getAbsolutePath(),
		// true);
		// tempImgUri = Uri.fromFile(file);
		// }
		// }
		// });
		chooseCountTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MyApplication.selectImages != null && MyApplication.selectImages.size() > 0) {
					Bundle extras = new Bundle();
					extras.putInt("image_index", 0);
					extras.putInt("type", 1);
					extras.putInt("max_count", maxChoosedCount);
					SharedFragmentActivity.startFragmentActivityForResult(ImagePickFragment.this,
							PreviewFragment.class, REQUEST_CODE_CAPTRUE_IMAGE, extras);
				} else {
					return;
				}
			}
		});
	}

	@OnClick(R.id.dirNameTv)
	public void OnClick(View v) {
		showChooseDirDialog();
	}

	// private void photograph() {
	// // 拍照
	// File dir = new File(Constants.BASE_DIR + "cache");
	// if (!dir.exists()) {
	// dir.mkdirs();
	// }
	//
	// File file = new File(dir, String.valueOf(new Date().getTime()) + ".jpg");
	// tempImgUri = Uri.fromFile(file);
	// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	// intent.putExtra(MediaStore.EXTRA_OUTPUT, tempImgUri);
	// startActivityForResult(intent, REQUEST_CODE_CAPTRUE_IMAGE);
	// }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE_CAPTRUE_IMAGE:
				Intent intent = new Intent();
				intent.putExtra("img_list", adapter.getSelectList());
				getActivity().setResult(Activity.RESULT_OK, intent);
				getActivity().finish();
				break;
			}
		} else if (resultCode == Activity.RESULT_FIRST_USER) {
			switch (requestCode) {
			case REQUEST_CODE_CAPTRUE_IMAGE:
				num = (Integer) data.getExtras().get("num");
				titleLayout.setRightText("完成" + "(" + num + "/" + maxChoosedCount + ")");
				chooseCountTv.setText(getString(R.string.photo_count, num));
				adapter.notifyDataSetChanged();
				break;
			}
		}else if (resultCode==Activity.RESULT_CANCELED){

		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adapter == null)
			getImageUri();
	}

	private Dialog dialog;

	private void showChooseDirDialog() {
		if (dialog == null) {
			dialog = new Dialog(getActivity(), R.style.dialog_fullscreen);
			ListView lv = new ListView(getActivity());
			lv.setBackgroundColor(0xcc000000);
			lv.setAdapter(new Adapter());
			lv.setPadding(
					0,
					(int) (getResources().getDimension(R.dimen.height_title) + getResources().getDimension(
							R.dimen.px_210)), 0, (int) getResources().getDimension(R.dimen.px_160));
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					for (int i = 0; i < dirMap.size(); i++) {
						dirMap.get(i).isShowing = (i == position);
					}
					dirNameTv.setText(dirMap.get(position).nameString);
					showUriList.clear();
					for (String path : imgUriList) {
						if (position != 0 && path.replace(dirMap.get(position).pathString, "").contains("/")) {
							continue;
						}
						showUriList.add(path);
						dialog.dismiss();
					}
					adapter.notifyDataSetChanged();
				}
			});
			dialog.setContentView(lv);
		}
		dialog.show();
	}

	@Override
	public void initData() {
		imgUriList = new ArrayList<String>();
		dirMap = new ArrayList<InfoDir>();
	}

	@Override
	public void refresh(InfoBaseJson infoJson) {
	}

	private void getImageUri() {
		new AsyncTask<Void, Void, Void>() {

			protected void onPreExecute() {
				showProgress(R.string.loading);
			};

			@Override
			protected Void doInBackground(Void... params) {
				try {
					final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
					final String orderBy = MediaStore.Images.Media._ID;

					Cursor imageCursor = getActivity().getContentResolver().query(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);

					if (imageCursor != null && imageCursor.getCount() > 0) {

						while (imageCursor.moveToNext()) {
							int dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);

							String sdcardPath = imageCursor.getString(dataColumnIndex);

							imgUriList.add(sdcardPath);
							String dirPath = sdcardPath.replaceAll("[^/]*$", "");
							InfoDir infoDir = null;
							for (InfoDir info : dirMap) {
								if (info.pathString.equals(dirPath)) {
									infoDir = info;
									infoDir.fileCount++;
									break;
								}
							}
							if (infoDir == null) {
								infoDir = new InfoDir();
								infoDir.firstFilePath = sdcardPath;
								infoDir.pathString = dirPath;
								infoDir.nameString = dirPath.substring(0, dirPath.length() - 1);
								infoDir.nameString = infoDir.nameString.replaceAll("^.*/", "");
								infoDir.fileCount++;
								dirMap.add(infoDir);
							}

						}
						InfoDir infoDir = new InfoDir();
						infoDir.firstFilePath = imgUriList.get(0);
						infoDir.pathString = infoDir.firstFilePath.replaceAll("[^/]*$", "");
						infoDir.nameString = "所有图片";
						infoDir.fileCount = imgUriList.size();
						infoDir.isShowing = true;
						dirMap.add(0, infoDir);
						Collections.sort(dirMap);
						currentDirString = infoDir.pathString;
						showUriList = new ArrayList<String>();
						showUriList.addAll(imgUriList);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				if (showUriList == null) {
					showUriList = new ArrayList<String>();
				}
				adapter = new ImagePickAdapter(ImagePickFragment.this, showUriList, maxChoosedCount, handler);
				imgContentGridV.setAdapter(adapter);
			};

		}.execute();

	}

	private class Adapter extends BaseAdapter {

		public Adapter() {
		}

		@Override
		public int getCount() {
			return dirMap.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			InfoDir infoDir = dirMap.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_dir, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.choosedImgView.setVisibility(infoDir.isShowing ? View.VISIBLE : View.GONE);
			ImageLoader.getInstance().displayImage("file://" + infoDir.firstFilePath, holder.imgV,
					MyApplication.options);
			holder.dirNameTv.setText(infoDir.nameString);
			holder.imgCountTv.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
			holder.imgCountTv.setText(infoDir.fileCount + "张");
			return convertView;
		}

		class ViewHolder {
			public TextView dirNameTv, imgCountTv;
			public ImageView imgV, choosedImgView;

			public ViewHolder(View convertView) {
				dirNameTv = (TextView) convertView.findViewById(R.id.dirNameTv);
				imgCountTv = (TextView) convertView.findViewById(R.id.imgCountTv);
				imgV = (ImageView) convertView.findViewById(R.id.imgV);
				choosedImgView = (ImageView) convertView.findViewById(R.id.choosedV);
			}
		}

	}
}
