set term postscript font 15 enhanced color
set output "x.eps" # chinh sua ten file output o day
#set title "x2"
set xlabel "x1"
set ylabel "x2"
set xrange [-30:40]
set yrange [-30:40]
#set key font ",14"
plot "x_without_control.txt" using 1:2 with lines linewidth 2 title "x without control", \
     "x_with_control.txt" using 1:2 with lines linewidth 2 linecolor rgb "black" title "x with control"
# chinh lai cai file input va ten cot de ve
# cot 1 la truc x, cot 2 la truc y
# with points la ve diem, with pointslines la ve diem va duong
