# Carousel

The idea is to have a screen made up of two main scrollable components: a ViewPager and a RecyclerView.

Despite the RecyclerView will scale up and down its children as they enter and leave an imaginary column in the middle of it while it's scrolled, the user won't be able to scroll the RecyclerView by dragging it, instead, the ViewPager will be the only one able to make the RecyclerView scroll by passing its offset to the RecyclerView as it's dragged by the user.
