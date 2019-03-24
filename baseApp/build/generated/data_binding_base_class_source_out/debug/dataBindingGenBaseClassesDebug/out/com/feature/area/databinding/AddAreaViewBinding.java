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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.motor.connect.feature.add.AddAreaViewModel;

public abstract class AddAreaViewBinding extends ViewDataBinding {
  @NonNull
  public final LinearLayout actionBarContainer;

  @NonNull
  public final Button btnSave;

  @NonNull
  public final ImageView imgWall;

  @NonNull
  public final EditText inputDetail;

  @NonNull
  public final EditText inputName;

  @NonNull
  public final EditText inputPhone;

  @NonNull
  public final TextView txtTitle;

  @NonNull
  public final TextView txtVan;

  @Bindable
  protected AddAreaViewModel mViewModel;

  protected AddAreaViewBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, LinearLayout actionBarContainer, Button btnSave, ImageView imgWall,
      EditText inputDetail, EditText inputName, EditText inputPhone, TextView txtTitle,
      TextView txtVan) {
    super(_bindingComponent, _root, _localFieldCount);
    this.actionBarContainer = actionBarContainer;
    this.btnSave = btnSave;
    this.imgWall = imgWall;
    this.inputDetail = inputDetail;
    this.inputName = inputName;
    this.inputPhone = inputPhone;
    this.txtTitle = txtTitle;
    this.txtVan = txtVan;
  }

  public abstract void setViewModel(@Nullable AddAreaViewModel viewModel);

  @Nullable
  public AddAreaViewModel getViewModel() {
    return mViewModel;
  }

  @NonNull
  public static AddAreaViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static AddAreaViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<AddAreaViewBinding>inflate(inflater, com.feature.area.R.layout.add_area_view, root, attachToRoot, component);
  }

  @NonNull
  public static AddAreaViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static AddAreaViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<AddAreaViewBinding>inflate(inflater, com.feature.area.R.layout.add_area_view, null, false, component);
  }

  public static AddAreaViewBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static AddAreaViewBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (AddAreaViewBinding)bind(component, view, com.feature.area.R.layout.add_area_view);
  }
}
