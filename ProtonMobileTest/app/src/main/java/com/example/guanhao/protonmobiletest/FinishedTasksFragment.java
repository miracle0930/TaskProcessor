package com.example.guanhao.protonmobiletest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FinishedTasksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FinishedTasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FinishedTasksFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private OnFragmentInteractionListener mListener;
    private ListView finishedTaskListView;
    private FinishedTasksAdapter adapter;

    public FinishedTasksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment FinishedTasksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FinishedTasksFragment newInstance() {
        FinishedTasksFragment fragment = new FinishedTasksFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        System.out.println(bundle);
        if (bundle != null && bundle.containsKey("task")) {
            Task taskData = (Task) getArguments().getSerializable("task");
            if (bundle.getString("status").equals("new")) {
                Util.FINISHED_TASKS.add(taskData);
                Util.FINISHED_SET.add(taskData.getName());
            } else {
                for (int i = 0; i < Util.FINISHED_TASKS.size(); i++) {
                    if (Util.FINISHED_TASKS.get(i).getName().equals(bundle.getString("name"))) {
                        Util.FINISHED_TASKS.set(i, taskData);
                    }
                }
            }
            Util.FINISHED_SET.remove(bundle.getString("name"));
            Util.FINISHED_SET.add(taskData.getName());
            this.adapter.notifyDataSetChanged();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_finished_tasks, container, false);
        finishedTaskListView = view.findViewById(R.id.finishedListView);
        adapter = new FinishedTasksAdapter(getContext(), Util.FINISHED_TASKS, inflater);
        finishedTaskListView.setAdapter(this.adapter);
        SwipeListViewOnTouchListener touchListener = new SwipeListViewOnTouchListener(finishedTaskListView, new SwipeListViewOnTouchListener.OnSwipeCallback(){
            @Override
            public void onSwipeLeft(ListView listView, int [] reverseSortedPositions) {
                restartTask(reverseSortedPositions[0]);
            }

            @Override
            public void onSwipeRight(ListView listView, int [] reverseSortedPositions) {

                restartTask(reverseSortedPositions[0]);

            }
        }, false, false);
        finishedTaskListView.setOnTouchListener(touchListener);
        finishedTaskListView.setOnScrollListener(touchListener.makeScrollListener());
        finishedTaskListView.setClickable(true);

        finishedTaskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CreateTaskActivity.class);
                intent.putExtra("task", Util.FINISHED_TASKS.get(position));
                intent.putExtra("from", "finished");
                startActivity(intent);
                return true;
            }
        });
        adapter.notifyDataSetChanged();
        return view;
    }

    public void restartTask(int index) {
        Task restart = Util.FINISHED_TASKS.get(index);
        restart.setStatus("Ready");
        Util.FINISHED_TASKS.remove(index);
        Util.FINISHED_SET.remove(restart.getName());
        Util.UNFINISHED_TASKS.add(restart);
        Util.UNFINISHED_SET.add(restart.getName());
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
