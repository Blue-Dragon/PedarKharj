package com.example.pedarkharj_edit3.classes;

import com.example.pedarkharj_edit3.pages.fragments.SliderItemFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SliderPagerAdapter extends FragmentPagerAdapter {
  public SliderPagerAdapter(@NonNull FragmentManager fm, int behavior) {
    super(fm, behavior);
  }

  @NonNull @Override
  public Fragment getItem(int position) {
    return SliderItemFragment.newInstance(position);
  }

  @Override
  public int getCount() {
    return 4;
  }
}