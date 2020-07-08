set term postscript font 15 enhanced color
set output "results_steps_5tm.eps"

set title "execution time (5 TM)"
set xlabel "number of steps"
set ylabel "time [s]"
#set xrange [-30:40]
#set yrange [-30:40]
#set key font ",14"
plot "static_composition_steps_5tm.csv"   using 4:5 with lines linewidth 2 linecolor rgb "black" title "static composition", \
     "dynamic_composition_steps_5tm.csv"  using 4:5 with lines linewidth 2 linecolor rgb "blue" title "dynamic composition", \
     "dynamic_composition2_steps_5tm.csv" using 4:5 with lines linewidth 2 linecolor rgb "green" title "dynamic composition v2"


set title "execution time (5 TM, RND)"
set xlabel "number of steps"
set ylabel "time [s]"
#set xrange [-30:40]
#set yrange [-30:40]
#set key font ",14"
plot "static_composition_rnd_steps_5tm.csv"   using 4:5 with lines linewidth 2 linecolor rgb "black" title "static composition", \
     "dynamic_composition_rnd_steps_5tm.csv"  using 4:5 with lines linewidth 2 linecolor rgb "blue" title "dynamic composition", \
     "dynamic_composition2_rnd_steps_5tm.csv" using 4:5 with lines linewidth 2 linecolor rgb "green" title "dynamic composition v2"