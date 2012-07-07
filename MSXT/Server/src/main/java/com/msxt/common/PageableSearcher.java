package com.msxt.common;

public abstract class PageableSearcher {
	protected boolean nextPageAvailable = false;
	
	public abstract FoggySearchCriteria getSearchCriteria();
	
	public abstract void doSearch();
	
	public void find() {
		getSearchCriteria().firstPage();
		doSearch();
    }

    public void nextPage() {
    	getSearchCriteria().nextPage();
    	doSearch();
    }

    public void previousPage() {
    	getSearchCriteria().previousPage();
    	doSearch();
    }
    
    public boolean isNextPageAvailable() {
        return nextPageAvailable;
    }

    public boolean isPreviousPageAvailable() {
        return getSearchCriteria().getPage() > 0;
    }
}
