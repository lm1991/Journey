package com.mesor.journey.framework;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.mesor.journey.R;

import java.util.List;

/**
 * 公用FragmentActivity
 * 
 * @ClassName SharedFragmentActivity
 */
public class SharedFragmentActivity extends BaseActivity {

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// super.onSaveInstanceState(outState);
	}

	public static final String INTENT_FRAGMENT_NAME = "intent_fragment_name";

	/**
	 * 启动一个fragment
	 * 
	 * @Title: startFragmentActivity
	 * @param fragmentClass
	 * @param extras
	 * @return void
	 */
	public static void startFragmentActivity(Context context, Class<? extends BaseFragment> fragmentClass, Bundle extras) {
		Intent intent = new Intent(context, SharedFragmentActivity.class);
		intent.putExtra(INTENT_FRAGMENT_NAME, fragmentClass);
		if (null != extras)
			intent.putExtras(extras);
		context.startActivity(intent);
	}

	public static void startFragmentActivity(Context context, Class<? extends BaseFragment> fragmentClass, Bundle extras, int flags) {
		Intent intent = new Intent(context, SharedFragmentActivity.class);
		intent.putExtra(INTENT_FRAGMENT_NAME, fragmentClass);
		intent.addFlags(flags);
		if (null != extras)
			intent.putExtras(extras);
		context.startActivity(intent);
	}

	/**
	 * 启动一个用于回调信息的fragment
	 * 
	 * @Title: startFragmentActivityForResult
	 * @param fragment
	 * @param fragmentClass
	 * @param requestCode
	 * @param extras
	 * @return void
	 */
	public static void startFragmentActivityForResult(BaseFragment fragment, Class<? extends BaseFragment> fragmentClass, int requestCode, Bundle extras) {
		Intent intent = new Intent(fragment.getActivity(), SharedFragmentActivity.class);
		intent.putExtra(INTENT_FRAGMENT_NAME, fragmentClass);
		if (null != extras)
			intent.putExtras(extras);
		// 2015/8/4 修改. 可以在fragment中onActivityResult接收返回信息
		// fragment.getActivity().startActivityForResult(intent, requestCode);

		getRoot(fragment).startActivityForResult(intent, requestCode);
	}

	public static Fragment getRoot(Fragment fragment) {
		Fragment fragment0 = fragment.getParentFragment();
		if (fragment0 != null)
			while (fragment0.getParentFragment() != null) {
				fragment0 = fragment0.getParentFragment();
			}
		return fragment0 == null ? fragment : fragment0;
	}

	/**
	 * Activity启动一个用于回调信息的fragment
	 * 
	 * @Title: startFragmentActivityForResult
	 * @param activity
	 * @param fragmentClass
	 * @param requestCode
	 * @param extras
	 * @return void
	 */
	public static void startFragmentActivityForResult(Activity activity, Class<? extends BaseFragment> fragmentClass, int requestCode, Bundle extras) {
		Intent intent = new Intent(activity, SharedFragmentActivity.class);
		intent.putExtra(INTENT_FRAGMENT_NAME, fragmentClass);
		if (null != extras)
			intent.putExtras(extras);
		// 2015/8/4 修改. 可以在fragment中onActivityResult接收返回信息
		// fragment.getActivity().startActivityForResult(intent, requestCode);
		activity.startActivityForResult(intent, requestCode);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Class<? extends BaseFragment> fragmentClass = (Class<? extends BaseFragment>) getIntent().getSerializableExtra(INTENT_FRAGMENT_NAME);
		if (fragmentClass != null) {
			setContentFragment(fragmentClass, getIntent().getExtras());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		FragmentManager fm = getSupportFragmentManager();
		int index = requestCode >> 16;
		if (index != 0) {
			index--;
			if (fm.getFragments() == null || index < 0 || index >= fm.getFragments().size()) {
				return;
			}
			Fragment frag = fm.getFragments().get(index);
			if (frag == null) {
			} else {
				handleResult(frag, requestCode, resultCode, data);
			}
			return;
		}
	}

	/**
	 * 递归调用，对所有子Fragement生效
	 * 
	 * @param frag
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	private void handleResult(Fragment frag, int requestCode, int resultCode, Intent data) {
		frag.onActivityResult(requestCode & 0xffff, resultCode, data);
		List<Fragment> frags = frag.getChildFragmentManager().getFragments();
		if (frags != null) {
			for (Fragment f : frags) {
				if (f != null)
					handleResult(f, requestCode, resultCode, data);
			}
		}
	}

}
