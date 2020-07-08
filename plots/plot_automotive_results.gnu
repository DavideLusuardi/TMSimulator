set term postscript font 15 enhanced color
set output "automotive_results.eps"

set title "execution time"
set xlabel "number of steps"
set ylabel "time [s]"
#set xrange [-30:40]
#set yrange [-30:40]
#set key font ",14"
plot "automotive_results_static_composition.csv"   using 1:2 with lines linewidth 2 linecolor rgb "black" title "static composition", \
     "automotive_results_dynamic_composition.csv"  using 1:2 with lines linewidth 2 linecolor rgb "blue" title "dynamic composition", \
     "automotive_results_dynamic_composition2.csv" using 1:2 with lines linewidth 2 linecolor rgb "green" title "dynamic composition v2"
