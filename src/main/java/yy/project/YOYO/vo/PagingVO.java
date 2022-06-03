package yy.project.YOYO.vo;


public class PagingVO {

    private int onePageRecord=10;  //1페이지 출력할 레코드 수
    private int pageNum=1;  //현재 페이지
    private int totalRecord; //전체 레코드 수
    private int totalPage;   //전체 페이지 수
    private int offsetIndex=0;
    private int onePageCount=5;  //한번에 표시할 페이지 수
    private int startPage = 1;

    private String searchWord; //검색어


    public int getOnePageRecord() {
        return onePageRecord;
    }
    public void setOnePageRecord(int onePageRecord) {
        this.onePageRecord = onePageRecord;
    }
    public int getPageNum() {
        return pageNum;
    }
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;

        offsetIndex = (pageNum-1)*onePageRecord;

        startPage = ((pageNum-1)/onePageCount*onePageCount)+1;
    }
    public int getTotalRecord() {
        return totalRecord;
    }
    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;

        if(totalRecord%onePageRecord==0) {
            totalPage = totalRecord/onePageRecord;
        }else {
            totalPage = totalRecord/onePageRecord+1;
        }
    }
    public int getTotalPage() {
        return totalPage;
    }
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getOffsetIndex() {
        return offsetIndex;
    }
    public void setOffsetIndex(int pageNum, int onePageRecord) {
        this.offsetIndex = (pageNum-1)*onePageRecord;
    }
    public int getStartPage() {
        return startPage;
    }
    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }
    public String getSearchWord() {
        return searchWord;
    }
    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }


}
