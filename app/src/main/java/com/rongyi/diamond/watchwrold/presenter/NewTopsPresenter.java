package com.rongyi.diamond.watchwrold.presenter;

import com.rongyi.diamond.baselibiary.base.mvp.BasePresenter;
import com.rongyi.diamond.networklibrary.app.NetworkApplication;
import com.rongyi.diamond.networklibrary.bean.NewsList;
import com.rongyi.diamond.networklibrary.model.NewTopsModel;
import com.rongyi.diamond.networklibrary.mvp.NewTopsContract;

import rx.Subscriber;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      16/10/8 下午11:59
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 16/10/8      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */
public class NewTopsPresenter extends BasePresenter implements NewTopsContract.Presenter {

    private final NewTopsContract.View mView;
    private NewTopsModel mNewTopsModel;
    private int mCurrentPage = 0;
    private int[] index = new int[]{0, 3, 5, 8};

    public NewTopsPresenter(NewTopsContract.View view) {
        mView = view;
    }

    private void getNewTops(int index) {
        if (mNewTopsModel == null) {
            mNewTopsModel = new NewTopsModel(NetworkApplication.getContext().getHttpApiMethods());
        }
        mSubscriptions.clear();
        Subscriber<NewsList> subscriber = new Subscriber<NewsList>() {
            @Override
            public void onCompleted() {
                mCurrentPage++;
            }

            @Override
            public void onError(Throwable e) {
                mView.onLoadError();
            }

            @Override
            public void onNext(NewsList imageData) {
                if (mCurrentPage == 0) {
                    mView.onDataRefresh(imageData.getNewsList());
                } else {
                    mView.onDataMore(imageData.getNewsList());
                }
            }
        };

        mNewTopsModel.getNews(subscriber, index);
        mSubscriptions.add(subscriber);
    }

    @Override
    public void getFirstNewTops() {
        mCurrentPage = 0;
        getMoreNewTops();
    }

    @Override
    public void getMoreNewTops() {
        getNewTops(mCurrentPage / 4 * 10 + index[mCurrentPage % 4]);
    }

}
