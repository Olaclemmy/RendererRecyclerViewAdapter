package com.github.vivchar.rendererrecyclerviewadapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Vivchar Vitaly on 20.10.17.
 */
public class CompositeViewState <VH extends CompositeViewHolder> implements ViewState<VH>, Serializable {

	@NonNull
	protected HashMap<Integer, ViewState> mViewStates;
	protected int mPosition;
	protected int mTopOffset;
	protected int mLeftOffset;

	public <VH extends CompositeViewHolder> CompositeViewState(@NonNull final VH holder) {
		mViewStates = holder.getAdapter().getStates();
		final RecyclerView.LayoutManager layoutManager = holder.getRecyclerView().getLayoutManager();
		if (layoutManager instanceof LinearLayoutManager) {
			/* To get rid of Parcelable, https://stackoverflow.com/a/35287828/4894238 */
			mPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
			final View item = holder.getRecyclerView().getChildAt(0);
			mTopOffset = (item == null) ? 0 : (item.getTop() - holder.getRecyclerView().getPaddingTop());
			mLeftOffset = (item == null) ? 0 : (item.getLeft() - holder.getRecyclerView().getPaddingLeft());
			Log.d("###", "save mPosition: " + mPosition + " mTopOffset: " + mTopOffset);
		} else {
			mPosition = 0;
			mTopOffset = 0;
		}
	}

	@Override
	public void restore(@NonNull final VH holder) {
		holder.getAdapter().setStates(mViewStates);

		final RecyclerView.LayoutManager layoutManager = holder.getRecyclerView().getLayoutManager();
		if (mPosition != -1 && layoutManager instanceof LinearLayoutManager) {
			Log.d("###", "restore mPosition: " + mPosition + " mTopOffset: " + mTopOffset);
			((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(mPosition, Math.max(mTopOffset, mLeftOffset));
		}
	}
}