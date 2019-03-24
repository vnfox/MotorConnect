package com.feature.area.databinding;

import android.databinding.Bindable;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.motor.connect.feature.details.AreaDetailViewModel;

public abstract class DetailViewBinding extends ViewDataBinding {
  @NonNull
  public final AppBarLayout appbar;

  @NonNull
  public final ImageView backdrop;

  @NonNull
  public final FloatingActionButton btnActionSetting;

  @NonNull
  public final CollapsingToolbarLayout collapsingToolbar;

  @NonNull
  public final CardView infoContainer;

  @NonNull
  public final CoordinatorLayout mainContent;

  @NonNull
  public final Toolbar toolbar;

  @NonNull
  public final TextView txtAreaDetail;

  @NonNull
  public final TextView txtAreaHistory;

  @NonNull
  public final TextView txtAreaName;

  @NonNull
  public final TextView txtAreaPhone;

  @NonNull
  public final TextView txtAreaScheduler;

  @NonNull
  public final TextView txtAreaStatus;

  @NonNull
  public final TextView txtAreaVan;

  @NonNull
  public final TextView txtAreaVanUsed;

  @NonNull
  public final TextView txtScheduleWorking;

  @NonNull
  public final TextView txtTimeStart;

  @NonNull
  public final TextView txtTimeWorking;

  @NonNull
  public final TextView txtVan;

  @NonNull
  public final CardView workingContainer;

  @Bindable
  protected AreaDetailViewModel mViewModel;

  protected DetailViewBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, AppBarLayout appbar, ImageView backdrop,
      FloatingActionButton btnActionSetting, CollapsingToolbarLayout collapsingToolbar,
      CardView infoContainer, CoordinatorLayout mainContent, Toolbar toolbar,
      TextView txtAreaDetail, TextView txtAreaHistory, TextView txtAreaName, TextView txtAreaPhone,
      TextView txtAreaScheduler, TextView txtAreaStatus, TextView txtAreaVan,
      TextView txtAreaVanUsed, TextView txtScheduleWorking, TextView txtTimeStart,
      TextView txtTimeWorking, TextView txtVan, CardView workingContainer) {
    super(_bindingComponent, _root, _localFieldCount);
    this.appbar = appbar;
    this.backdrop = backdrop;
    this.btnActionSetting = btnActionSetting;
    this.collapsingToolbar = collapsingToolbar;
    this.infoContainer = infoContainer;
    this.mainContent = mainContent;
    this.toolbar = toolbar;
    this.txtAreaDetail = txtAreaDetail;
    this.txtAreaHistory = txtAreaHistory;
    this.txtAreaName = txtAreaName;
    this.txtAreaPhone = txtAreaPhone;
    this.txtAreaScheduler = txtAreaScheduler;
    this.txtAreaStatus = txtAreaStatus;
    this.txtAreaVan = txtAreaVan;
    this.txtAreaVanUsed = txtAreaVanUsed;
    this.txtScheduleWorking = txtScheduleWorking;
    this.txtTimeStart = txtTimeStart;
    this.txtTimeWorking = txtTimeWorking;
    this.txtVan = txtVan;
    this.workingContainer = workingContainer;
  }

  public abstract void setViewModel(@Nullable AreaDetailViewModel viewModel);

  @Nullable
  public AreaDetailViewModel getViewModel() {
    return mViewModel;
  }

  @NonNull
  public static DetailViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static DetailViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<DetailViewBinding>inflate(inflater, com.feature.area.R.layout.detail_view, root, attachToRoot, component);
  }

  @NonNull
  public static DetailViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static DetailViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<DetailViewBinding>inflate(inflater, com.feature.area.R.layout.detail_view, null, false, component);
  }

  public static DetailViewBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static DetailViewBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (DetailViewBinding)bind(component, view, com.feature.area.R.layout.detail_view);
  }
}
