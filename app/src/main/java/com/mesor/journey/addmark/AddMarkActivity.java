package com.mesor.journey.addmark;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.mesor.journey.R;
import com.mesor.journey.framework.BaseActivity;
import com.mesor.journey.framework.ToolLayout;

/**
 * Created by Limeng on 2016/9/5.
 */
public class AddMarkActivity extends BaseActivity {

    private final int FRAGMENT_CHOOSE_POINT = 1;
    private final int FRAGMENT_EDIT_DETAIL = 2;

    private int current_fragment = FRAGMENT_CHOOSE_POINT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFragment(ChoosePointFragment.class);
        setTitle("设置标记位置");
        setOnTitleListener(new ToolLayout.OnTitleListener() {
            @Override
            public void clickBack() {
                switch (current_fragment) {
                    case FRAGMENT_CHOOSE_POINT:
                        finish();
                        break;
                    case FRAGMENT_EDIT_DETAIL:
                        current_fragment = FRAGMENT_CHOOSE_POINT;
                        setTitle("设置标记位置");
                        invalidateOptionsMenu();
                        getSupportFragmentManager().popBackStackImmediate();
                        break;
                }
            }

            @Override
            public void clickRight(MenuItem item) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_next_step, menu);
        if (current_fragment == FRAGMENT_EDIT_DETAIL) {
            menu.findItem(R.id.nextStepTv).setTitle("提交");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (current_fragment) {
            case FRAGMENT_CHOOSE_POINT:
                if (((ChoosePointFragment) getVisibleFragment()).getCurrentMarker() != null) {
                    current_fragment = FRAGMENT_EDIT_DETAIL;
                    setContentFragment(DetailFragment.class);
                    invalidateOptionsMenu();
                }
                break;
            case FRAGMENT_EDIT_DETAIL:
                break;
        }
        //TODO
        return super.onOptionsItemSelected(item);
    }
}
