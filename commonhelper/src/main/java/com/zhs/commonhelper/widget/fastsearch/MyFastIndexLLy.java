package com.zhs.commonhelper.widget.fastsearch;

import android.app.Activity;
import android.content.Context;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.zhs.commonhelper.R;
import com.zhs.commonhelper.widget.fastsearch.searchedt.SearchEditText;
import com.zhs.commonhelper.widget.fastsearch.searchedt.SortModel;
import com.zhs.commonhelper.widget.fastsearch.searchedt.Trans2PinYinUtil;
import com.zhs.commonhelper.widget.fastsearch.suspensionIndexbar.ConfirmCallback;
import com.zhs.commonhelper.widget.fastsearch.suspensionIndexbar.adapter.CityAdapter;
import com.zhs.commonhelper.widget.fastsearch.suspensionIndexbar.decoration.DividerItemDecoration;
import com.zhs.commonhelper.widget.fastsearch.suspensionIndexbar.model.CityBean;
import com.zhs.commonhelper.widget.fastsearch.suspensionIndexbar.model.SearchModel;
import com.zhs.commonhelper.widget.fastsearch.suspensionIndexbar.utils.HeaderRecyclerAndFooterWrapperAdapter;
import com.zhs.commonhelper.widget.fastsearch.suspensionIndexbar.utils.SearchViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * 类似通讯录快速索引控件
 * Created by zhs89 on 2017/12/12.
 */

public class MyFastIndexLLy extends RelativeLayout {
    private Context mContext;
    private View mView;
    private SearchEditText editText;
    private TextView rightTxt,leftTxt;
    private List<SearchModel> searchList = new ArrayList<>();
    public List<CityBean> searchContactList = new ArrayList<>();
    private RecyclerView searchRecyclerView;

    private CityAdapter mAdapter;//recyclerview的适配器
    private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;
    private LinearLayoutManager layoutManager;

    private SuspensionDecoration mDecoration;
    /**
     * 右侧边栏导航区域
     */
    private IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    private TextView mTvSideBarHint;

    public MyFastIndexLLy(Context context) {
        this(context, null);
    }

    public MyFastIndexLLy(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        //在构造函数中将Xml中定义的布局解析出来。
        mView = LayoutInflater.from(context).inflate(R.layout.module_fastindex, this, false);
        addView(mView);

        editText = (SearchEditText)mView.findViewById(R.id.module_fastindex_searchedt);
        searchRecyclerView =(RecyclerView)mView.findViewById(R.id.module_fastindex_searchrv);
        layoutManager = new LinearLayoutManager(mContext);
        searchRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new CityAdapter(mContext, searchContactList, R.layout.item_city);
        //设置头部快捷按钮适配器
        mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(mAdapter) {
            @Override
            protected void onBindHeaderHolder(SearchViewHolder holder, int headerPos, int layoutId, Object o) {
                holder.setText(R.id.tvCity, (String) o);
            }
        };
        //这个添加的是类似快速索引。不归右侧滑动字母管
        //mHeaderAdapter.setHeaderView(R.layout.item_city, "测试头部");

        searchRecyclerView.setAdapter(mHeaderAdapter);
        //添加所有选项头部显示当前字母
        searchRecyclerView.addItemDecoration(mDecoration = new SuspensionDecoration(mContext, searchContactList).setHeaderViewCount(mHeaderAdapter.getHeaderViewCount()));
        mDecoration.setColorTitleBg(getResources().getColor(R.color.search_bg));//设置当前字母背景颜色
        //如果add两个，那么按照先后顺序，依次渲染。
        DividerItemDecoration dividerItemDecoration;
        //添加item之间的分割线，现在用item内部的
        //searchRecyclerView.addItemDecoration(dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        //使用indexBar
        mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);//HintTextView
        mIndexBar = (IndexBar) findViewById(R.id.indexBar);//IndexBar
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(false)//设置需要真实的索引
                .setmLayoutManager(layoutManager);//设置RecyclerView的LayoutManager

        //编辑改变內容进行搜索
        editText.setOnSearchClickListener(new SearchEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view, String keyword) {
                //搜索条件//去除空格
                keyword = keyword.replace(" ", "");
                searchContactList.clear();
                //被搜索的数据
                List<SortModel> sortModels = new ArrayList<>();

                for (SearchModel model : searchList) {
                    SortModel sortModel = new SortModel();
                    //name之后还要用所以，不能在这里去除空格
                    sortModel.setSearchNameStr(model.getSearchNameStr());//按名字搜索
                    sortModel.setSearchId(model.getSearchId());//按编号搜索
                    sortModel.sortToken = Trans2PinYinUtil.getSortToken(model.getSearchNameStr());//按名字的拼音搜索
                    sortModels.add(sortModel);
                }

                //进行搜索
                sortModels = Trans2PinYinUtil.searchContact(keyword, sortModels);
                //遍历搜索结果
                for (SortModel model : sortModels) {
                    CityBean searchBean = (CityBean) new CityBean(model.getSearchNameStr(), model.getSearchId()).setTop(false);
                    searchContactList.add(searchBean);

//                    String[] strs = Trans2PinYinUtil.myTrans2PinYin(model.getSearchNameStr());
//                    if(strs!=null && strs.length==2) {
//                        String allLetter = strs[0].replace(",", "");
//                        String[] letters = strs[0].split(",");
//                        for(int i = 0; i< letters.length; i++){
//
//                        }
//                        //全拼包含输入的字母，此时去检测输入的字母是否是完整的一个中文的全拼或者多个
//                        if (allLetter.contains(keyword)){
//                            int i = 0;
//                            for(i = 0; i< letters.length; i++){
//                                if(letters[i] == keyword){
//                                    break;
//                                }
//                            }
//                            //说明被搜索中文的全拼中中找到相同的全拼的中文
//                            if(i!=letters.length){
//                                CityBean searchBean = (CityBean) new CityBean(model.getSearchNameStr()).setTop(false);
//                                searchContactList.add(searchBean);
//                            }
//                            //也可能是多个中文全拼
//                            else if(true){
//
//                            }
//                        }//中文包含
//                        else if (model.getSearchNameStr().contains(keyword)) {
//                            CityBean searchBean = (CityBean) new CityBean(model.getSearchNameStr()).setTop(false);
//                            searchContactList.add(searchBean);
//                        }//简拼包含
//                        else if(strs[1].contains(keyword)){
//                            CityBean searchBean = (CityBean) new CityBean(model.getSearchNameStr()).setTop(false);
//                            searchContactList.add(searchBean);
//                        }
//                    }
                }
                mIndexBar.setmSourceDatas(searchContactList)//设置数据
                        .invalidate();
                mDecoration.setmDatas(searchContactList);
                mHeaderAdapter.notifyDataSetChanged();
            }
        });

        rightTxt = (TextView)mView.findViewById(R.id.module_fastindex_headerright);
        rightTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)mContext).finish();
            }
        });
        leftTxt = (TextView)findViewById(R.id.module_fastindex_headerleft);

    }

    public void initView(List<SearchModel> searchList){
        initView(searchList, false);
    }

    /**
     * 初始化数据
     * @param searchList
     * isVisibilityBindNfc 是否显示绑定过nfc
     */
    public void initView(List<SearchModel> searchList, boolean isVisibilityBindNfc){
        searchContactList.clear();
        this.searchList = searchList;//z指针改变了，所以需要重新设置适配器
        for(int i = 0; i < searchList.size(); i++){
            CityBean searchBean = (CityBean) new CityBean(searchList.get(i).getSearchNameStr(), searchList.get(i).getSearchId(), searchList.get(i).getIsRightImg()).setTop(false);
            searchContactList.add(searchBean);
        }

        mIndexBar.setmSourceDatas(searchContactList)//设置数据
                .invalidate();
        mDecoration.setmDatas(searchContactList);

        if(isVisibilityBindNfc){
            mAdapter.isVisibilityBindNfc(true);
        }else{
            mAdapter.isVisibilityBindNfc(false);
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 设置列表item点击事件，不包括头部的快捷按钮
     * @param callback 这里callback里返回的pos是界面上显示的位置和searchContactList对应和searchList没关系
     */
    public void setRecyclerClick(ConfirmCallback callback){
        mAdapter.setConfirmCallback(callback);
    }

    /**
     * 设置头部左侧为当前要选择的内容
     * @param string
     */
    public void setLeftTxt(String string, OnClickListener onClickListener){
        leftTxt.setText(string);
        leftTxt.setOnClickListener(onClickListener);
    }

    /**
     * 设置头部右侧按钮
     * @param string
     */
    public void setRightTxt(String string, OnClickListener onClickListener){
        rightTxt.setText(string);
        rightTxt.setOnClickListener(onClickListener);
    }
}
