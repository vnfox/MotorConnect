package com.feature.area.databinding;

import android.databinding.Bindable;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import com.motor.connect.feature.demo.DemoViewModel;

public abstract class HomeViewBinding extends ViewDataBinding {
  @NonNull
  public final Button btnDemo;

  @NonNull
  public final Button btnTest;

  @NonNull
  public final CircularProgressIndicator circularProgress;

  @NonNull
  public final LinearLayout complianceLayout;

  @NonNull
  public final TextView txtDemo;

  @Bindable
  protected DemoViewModel mViewModel;

  protected HomeViewBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, Button btnDemo, Button btnTest,
      CircularProgressIndicator circularProgress, LinearLayout complianceLayout, TextView txtDemo) {
    super(_bindingComponent, _root, _localFieldCount);
    this.btnDemo = btnDemo;
    this.btnTest = btnTest;
    this.circularProgress = circularProgress;
    this.complianceLayout = complianceLayout;
    this.txtDemo = txtDemo;
  }

  public abstract void setViewModel(@Nullable DemoViewModel viewModel);

  @Nullable
  public DemoViewModel getViewModel() {
    return mViewModel;
  }

  @NonNull
  public static HomeViewBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static HomeViewBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<HomeViewBinding>inflate(inflater, com.feature.area.R.layout.home_view, root, attachToRoot, component);
  }

  @NonNull
  public static HomeViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static HomeViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<HomeViewBinding>inflate(inflater, com.feature.area.R.layout.home_view, null, false, component);
  }

  public static HomeViewBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static HomeViewBinding bind(@NonNull View view, @Nullable DataBindingComponent component) {
    return (HomeViewBinding)bind(component, view, com.feature.area.R.layout.home_view);
  }
}
