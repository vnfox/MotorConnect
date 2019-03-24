package com.feature.area.databinding;

import android.databinding.Bindable;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.motor.connect.feature.main.UserViewModel;

public abstract class ActivityMainBinding extends ViewDataBinding {
  @NonNull
  public final LinearLayout bottomContainer;

  @NonNull
  public final TextView btnAdd;

  @NonNull
  public final TextView btnHome;

  @NonNull
  public final TextView btnNotify;

  @NonNull
  public final TextView btnSetting;

  @NonNull
  public final RecyclerView rcArea;

  @NonNull
  public final RecyclerView recyclerView;

  @NonNull
  public final TextView txtEmpty;

  @NonNull
  public final TextView txtTitle;

  @Bindable
  protected UserViewModel mViewModel;

  protected ActivityMainBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, LinearLayout bottomContainer, TextView btnAdd, TextView btnHome,
      TextView btnNotify, TextView btnSetting, RecyclerView rcArea, RecyclerView recyclerView,
      TextView txtEmpty, TextView txtTitle) {
    super(_bindingComponent, _root, _localFieldCount);
    this.bottomContainer = bottomContainer;
    this.btnAdd = btnAdd;
    this.btnHome = btnHome;
    this.btnNotify = btnNotify;
    this.btnSetting = btnSetting;
    this.rcArea = rcArea;
    this.recyclerView = recyclerView;
    this.txtEmpty = txtEmpty;
    this.txtTitle = txtTitle;
  }

  public abstract void setViewModel(@Nullable UserViewModel viewModel);

  @Nullable
  public UserViewModel getViewModel() {
    return mViewModel;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityMainBinding>inflate(inflater, com.feature.area.R.layout.activity_main, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityMainBinding>inflate(inflater, com.feature.area.R.layout.activity_main, null, false, component);
  }

  public static ActivityMainBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityMainBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityMainBinding)bind(component, view, com.feature.area.R.layout.activity_main);
  }
}
