package com.example.guanhao.protonmobiletest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UnfinishedTasksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UnfinishedTasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnfinishedTasksFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private ListView unfinishedTaskListView;
    private UnFinishedTasksAdapter adapter;
    int notificationId = 0;

    public UnfinishedTasksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment UnfinishedTasksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UnfinishedTasksFragment newInstance() {
        UnfinishedTasksFragment fragment = new UnfinishedTasksFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        System.out.println("bundle in unfinished " + bundle);
        if (bundle != null && bundle.containsKey("task")) {
            Task taskData = (Task) getArguments().getSerializable("task");
            if (bundle.getString("status").equals("new")) {
                Util.UNFINISHED_SET.add(taskData.getName());
                Util.UNFINISHED_TASKS.add(taskData);
            } else {
                for (int i = 0; i < Util.UNFINISHED_TASKS.size(); i++) {
                    if (Util.UNFINISHED_TASKS.get(i).getName().equals(bundle.getString("name"))) {
                        Util.UNFINISHED_TASKS.set(i, taskData);
                    }
                }
            }
            Util.UNFINISHED_SET.remove(bundle.getString("name"));
            Util.UNFINISHED_SET.add(taskData.getName());
            this.adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unfinished_tasks, container, false);
        unfinishedTaskListView = view.findViewById(R.id.unfinishedListView);
        adapter = new UnFinishedTasksAdapter(getContext(), Util.UNFINISHED_TASKS, inflater);
        unfinishedTaskListView.setAdapter(this.adapter);
        SwipeListViewOnTouchListener touchListener = new SwipeListViewOnTouchListener(unfinishedTaskListView, new SwipeListViewOnTouchListener.OnSwipeCallback(){

            @Override
            public void onSwipeLeft(ListView listView, int [] reverseSortedPositions) {
                synchronized (Util.UNFINISHED_TASKS) {
                    final Task task = Util.UNFINISHED_TASKS.get(reverseSortedPositions[0]);
                    CountDownTimer timer = new CountDownTimer(10000, 1000) {
                        @Override
                        public void onTick(long m) {
                            task.setStatus("Start in " + m / 1000 + "s");
                            adapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onFinish() {
                            taskFinished(task);
                            adapter.notifyDataSetChanged();
                        }
                    };
                    task.setTimer(timer);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            task.getTimer().start();
                        }
                    }).start();
                    task.setButtonColor(Color.RED);
                    task.setButtonText("Cancel");
                }

            }

            @Override
            public void onSwipeRight(ListView listView, int [] reverseSortedPositions) {
                taskFinished(Util.UNFINISHED_TASKS.get(reverseSortedPositions[0]));
            }
        }, false, false);

        unfinishedTaskListView.setOnTouchListener(touchListener);
        unfinishedTaskListView.setOnScrollListener(touchListener.makeScrollListener());
        unfinishedTaskListView.setClickable(true);

        unfinishedTaskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CreateTaskActivity.class);
                intent.putExtra("task", Util.UNFINISHED_TASKS.get(position));
                intent.putExtra("from", "unfinished");
                startActivity(intent);
                return true;
            }
        });
        return view;

    }

    public void taskFinished(Task finished) {
        String info = "Finished in " + (int) (Math.random() * 100) + "s.";
        finished.setStatus(info);
        finished.setTimer(null);
        Util.UNFINISHED_TASKS.remove(finished);
        Util.UNFINISHED_SET.remove(finished.getName());
        Util.FINISHED_TASKS.add(finished);
        Util.FINISHED_SET.add(finished.getName());
        showNotification(finished.getName(), info);
    }

    public void showNotification(String name, String info) {
        Intent intent = new Intent(getActivity(), Notification.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pIntent = PendingIntent.getActivity(getContext(), (int) System.currentTimeMillis(), intent, 0);

        Notification n  = new Notification.Builder(getContext())
                .setContentTitle("Task '" + name + "' Finished!")
                .setContentText(info)
                .setSmallIcon(R.drawable.common_full_open_on_phone)
                .setContentIntent(pIntent)
                .setAutoCancel(false).build();
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId++, n);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && adapter != null) {
            adapter.notifyDataSetChanged();
        }
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
