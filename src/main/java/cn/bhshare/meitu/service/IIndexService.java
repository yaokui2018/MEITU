package cn.bhshare.meitu.service;

import javax.servlet.http.HttpServletRequest;

public interface IIndexService {

	void getModelById(HttpServletRequest request, Integer id);

	void getPhoto(HttpServletRequest request, Integer id);

	void index(HttpServletRequest request, String source, String search, Integer page, Integer size);

    void models(HttpServletRequest request, Integer page, Integer size);
}