package summerVocation.gitpack.RecylerView

interface ISearchHistoryRecylcerview {
    //검색아이템 삭제 버튼 클릭
    fun onSearchItemDeleteClicked(position: Int)

    //검색버튼 클릭
    fun onSearchItemClicked(position: Int)
}