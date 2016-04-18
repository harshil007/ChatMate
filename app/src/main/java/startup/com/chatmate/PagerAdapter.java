package startup.com.chatmate;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    OfflineFragment tab1;
    TabFragment2 tab2;
    TabFragment3 tab3;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:

                tab1 = new OfflineFragment();
                return tab1;
            case 1:
                tab2 = new TabFragment2();
                return tab2;
            case 2:
                tab3 = new TabFragment3();

                return tab3;
            default:
                return null;
        }
    }

    public Fragment getFragment(int position){
        switch (position) {
            case 0:
                return tab1;
            case 1:
                return tab2;
            case 2:
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}