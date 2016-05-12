package com.khyzhun.sasha.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.List;

public class CrimeListFragment extends ListFragment {

    private List<Crime> crimes;
    private static final String TAG = "CrimeListFragment";
    private boolean subtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subtitleVisible = true;
        setRetainInstance(true);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.crimes_title);
        crimes = CrimeLab.getInstance(getActivity()).getCrimes();

        CrimeAdapter crimeArrayAdapter = new CrimeAdapter(crimes);
        setListAdapter(crimeArrayAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = super.onCreateView(layoutInflater, parent, savedInstanceState);

        if (subtitleVisible) {
            ((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle);
        }

        ListView listView = (ListView)view.findViewById(android.R.id.list);
        wireListView(listView);
        return view;
    }

    private void wireListView(ListView listView) {
        if (AndroidVersionHelper.isHoneycombOrHigher()) {
            configureListViewForMultiChoiceMode(listView);
        } else {
            registerForContextMenu(listView);
        }

    }

    private void configureListViewForMultiChoiceMode(ListView listView) {
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater menuInflater = actionMode.getMenuInflater();
                menuInflater.inflate(R.menu.crime_list_item_context, menu);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.menu_item_delete_crime:
                        CrimeAdapter crimeAdapter = (CrimeAdapter)getListAdapter();
                        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
                        for (int i= crimeAdapter.getCount()-1; i>=0; i--) {
                            if (getListView().isItemChecked(i)) {
                                crimeLab.deleteCrime(crimeAdapter.getItem(i));
                            }
                        }
                        actionMode.finish();
                        crimeAdapter.notifyDataSetChanged();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        startCrimePagerActivity(position);
    }

    private void startCrimePagerActivity(int position) {
        Crime crime = ((CrimeAdapter)getListAdapter()).getItem(position);
        Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_crime_list, menu);
        setMenuItemSubtitle(menu);
    }

    private void setMenuItemSubtitle(Menu menu) {
        MenuItem subtitleMenuItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (subtitleVisible && subtitleMenuItem != null) {
            subtitleMenuItem.setTitle(R.string.subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.getInstance(getActivity()).addCrime(crime);

                Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
                intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
                startActivityForResult(intent, 0);

                return true;
            case R.id.menu_item_show_subtitle:
                if (((ActionBarActivity)getActivity()).getSupportActionBar().getSubtitle() == null) {
                    subtitleVisible = true;
                    ((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle);
                    menuItem.setTitle(R.string.hide_subtitle);

                } else {
                    subtitleVisible = false;
                    ((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle(null);
                    menuItem.setTitle(R.string.show_subtitle);
                }
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, contextMenu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuItem.getMenuInfo();
        int position = info.position;
        CrimeAdapter crimeAdapter = (CrimeAdapter)getListAdapter();
        Crime crime = crimeAdapter.getItem(position);

        switch (menuItem.getItemId()) {
            case R.id.menu_item_delete_crime:
                CrimeLab.getInstance(getActivity()).deleteCrime(crime);
                crimeAdapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(menuItem);
    }

    private class CrimeAdapter extends ArrayAdapter<Crime> {

        public CrimeAdapter(List<Crime> crimes) {
            super(getActivity(), 0, crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = createView();
            }

            Crime crime = getItem(position);

            populateTitle(convertView, crime);
            populateDate(convertView, crime);
            populateSolved(convertView, crime);

            return convertView;
        }

        private void populateSolved(View convertView, Crime crime) {
            CheckBox solvedCheckBox = (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(crime.isSolved());
        }

        private void populateDate(View convertView, Crime crime) {
            TextView dateTextView = (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
            SimpleDateFormat dateFormatter = getSimpleDateFormat();
            dateTextView.setText(dateFormatter.format(crime.getDate()));
        }

        private SimpleDateFormat getSimpleDateFormat() {
            return new SimpleDateFormat("MM/dd/yyyy");
        }

        private void populateTitle(View convertView, Crime crime) {
            TextView titleTextView = (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(crime.getTitle());
        }

        private View createView() {
            return ((ActionBarActivity)getActivity()).getLayoutInflater().inflate(R.layout.list_item_crime, null);
        }
    }
}