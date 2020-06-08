set term postscript font 15 enhanced color
set output "aw.eps" # chinh sua ten file output o day
#set title "a(t)"
set xlabel "t"
set ylabel "a(t)"
set xrange [0:2]
set yrange [-1:1.5]
#set key font ",14"
plot "aw_without_control.txt" using 1:2 with lines linewidth 2 title "aw without control", \
     "aw_with_control.txt" using 1:2 with lines linewidth 2 linecolor rgb "black" title "aw with control"
# chinh lai cai file input va ten cot de ve
# cot 1 la truc x, cot 2 la truc y
# with points la ve diem, with pointslines la ve diem va duong
