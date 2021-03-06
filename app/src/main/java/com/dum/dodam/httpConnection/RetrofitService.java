package com.dum.dodam.httpConnection;

import com.dum.dodam.Community.dataframe.ArticleCommentResponse;
import com.dum.dodam.Community.dataframe.ArticleListResponse;
import com.dum.dodam.Community.dataframe.ArticleResponse;
import com.dum.dodam.Community.dataframe.FeedResult;
import com.dum.dodam.Contest.dataframe.ContestListResponse;
import com.dum.dodam.Home.dataframe.HotArticleResponse;
import com.dum.dodam.Home.dataframe.MyCommunityResponse;
import com.dum.dodam.Login.Data.LoginResponse;
import com.dum.dodam.Login.Data.SearchResponse;
import com.dum.dodam.Mypage.Dataframe.MyWorkResponse;
import com.dum.dodam.Mypage.Dataframe.SurveyResponse;
import com.dum.dodam.Univ.dataframe.LiveShowResponse;
import com.dum.dodam.Univ.dataframe.MajorResponse;
import com.dum.dodam.Univ.dataframe.RankingResponse2;
import com.dum.dodam.Univ.dataframe.UnivArticleResponse;
import com.dum.dodam.Univ.dataframe.UnivLogoResponse;
import com.dum.dodam.Univ.dataframe.UnivResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("/auth/registerFCM")
    Call<BaseResponse> registerFCM(@Query("token") String token);

    @GET("/auth/login")
    Call<LoginResponse> kakaoLogin(@Query("id") long id, @Query("token") String token);

    @GET("/auth/logout")
    Call<BaseResponse> kakaoLogout();

    @GET("/auth/withdraw")
    Call<BaseResponse> kakaoSignOut(@Query("id") long id);

    @POST("/auth/kakaoSignup")
    Call<BaseResponse> registerKAKAO(@Body JsonObject body);

    @GET("/mypage/surveyLink")
    Call<SurveyResponse> getSurveyLink();

    @GET("/mypage/myWork")
    Call<MyWorkResponse> getMyWork();

    @GET("/search/schoolList")
    Call<SearchResponse> searchSchoolName(@Query("schoolName") String schoolName);

    @GET("/search/univList")
    Call<UnivResponse> searchCollageName(@Query("univName") String univName);

    @GET("/search/majorList")
    Call<MajorResponse> searchMajorName(@Query("majorName") String majorName);

    @GET("/univ/logoImage")
    Call<UnivLogoResponse> getUnivLogo(@Query("univID") int univID);

    @GET("/article/latestArticleList")
    Call<MyCommunityResponse> getMyCommunity();

    @GET("/article/hotArticleList")
    Call<HotArticleResponse> getHotArticle();

    @GET("/article/read")
    Call<ArticleResponse> readArticle(@Query("articleID") int articleID, @Query("communityType") int communityType, @Query("communityID") int communityID);

    @GET("/article/modifyHeart")
    Call<BaseResponse> modifyHeart(@Query("articleID") int articleID, @Query("communityType") int communityType, @Query("communityID") int communityID, @Query("op") int op);

    @GET("/reply/read")
    Call<ArticleCommentResponse> readArticleComment(@Query("articleID") int articleID, @Query("communityType") int communityType, @Query("communityID") int communityID);

    @POST("/reply/write")
    Call<BaseResponse> uploadComment(@Body JsonObject body);

    @GET("/reply/delete")
    Call<BaseResponse> deleteComment(@Query("communityType") int communityType, @Query("communityID") int communityID, @Query("articleID") int articleID, @Query("replyID") int replyID, @Query("isRereply") int isReReply);

    @POST("/article/write")
    Call<BaseResponse> writeArticle(@Body JsonObject body);

    @GET("/article/delete")
    Call<BaseResponse> deleteArticle(@Query("communityType") int communityType, @Query("communityID") int communityID, @Query("articleID") int articleID);

    @GET("/article/report")
    Call<BaseResponse> reportArticle(@Query("communityType") int communityType, @Query("communityID") int communityID, @Query("articleID") int articleID);

    @GET("/article/articleList")
    Call<ArticleListResponse> readArticleList(@Query("communityType") int communityType, @Query("communityID") int communityID, @Query("writtenAfter") String writtenAfter);

    @GET("/contest/getList")
    Call<ContestListResponse> getContestList(@Query("storedDate") String storedDate);

    @GET("/article/articleList")
    Call<UnivArticleResponse> readUnivNews(@Query("communityType") int communityType, @Query("communityID") int communityID, @Query("writtenAfter") String writtenAfter);

    @GET("/article/articleList")
    Call<UnivArticleResponse> readUnivCommunity(@Query("communityType") int communityType, @Query("communityID") int communityID, @Query("writtenAfter") String writtenAfter);

    @GET("/univ/liveShowList")
    Call<LiveShowResponse> readLiveShowList(@Query("writtenAfter") String writtenAfter);

    @GET("/univ/viewUniv")
    Call<BaseResponse> getViewUniv(@Query("univID") int id);

    @GET("/univ/viewMajor")
    Call<BaseResponse> getViewMajor(@Query("majorID") int id);

    @GET("/univ/ranking")
    Call<RankingResponse2> loadRanking();

    @GET("/17841442985795349/media")
    Call<FeedResult> getFeeds(@Query("fields") String fields, @Query("access_token") String access_token);

    @GET("/{id}/children")
    Call<FeedResult> getFeedDetails(@Path("id") String id, @Query("fields") String fields, @Query("access_token") String access_token);
}
