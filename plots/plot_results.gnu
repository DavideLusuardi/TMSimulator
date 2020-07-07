set term postscript font 15 enhanced color
set output "results_4.eps"

set title "memory usage"
set xlabel "number of tag machines"
set ylabel "memory [byte]"
#set xrange [-30:40]
#set yrange [-30:40]
#set key font ",14"
plot "static_composition.csv"   using 1:6 with lines linewidth 2 linecolor rgb "black" title "static composition", \
     "dynamic_composition.csv"  using 1:6 with lines linewidth 2 linecolor rgb "blue" title "dynamic composition", \
     "dynamic_composition2.csv" using 1:6 with lines linewidth 2 linecolor rgb "green" title "dynamic composition v2"

set title "execution time"
set xlabel "number of tag machines"
set ylabel "time [ns]"
#set xrange [-30:40]
#set yrange [-30:40]
#set key font ",14"
plot "static_composition.csv"   using 1:5 with lines linewidth 2 linecolor rgb "black" title "static composition", \
     "dynamic_composition.csv"  using 1:5 with lines linewidth 2 linecolor rgb "blue" title "dynamic composition", \
     "dynamic_composition2.csv" using 1:5 with lines linewidth 2 linecolor rgb "green" title "dynamic composition v2"


set title "execution time (4 TM)"
set xlabel "number of steps"
set ylabel "time [ns]"
#set xrange [-30:40]
#set yrange [-30:40]
#set key font ",14"
plot "static_composition_steps.csv"   using 4:5 with lines linewidth 2 linecolor rgb "black" title "static composition", \
     "dynamic_composition_steps.csv"  using 4:5 with lines linewidth 2 linecolor rgb "blue" title "dynamic composition", \
     "dynamic_composition2_steps.csv" using 4:5 with lines linewidth 2 linecolor rgb "green" title "dynamic composition v2"



set title "memory usage (RND)"
set xlabel "number of tag machines"
set ylabel "memory [byte]"
#set xrange [-30:40]
#set yrange [-30:40]
#set key font ",14"
plot "static_composition_rnd.csv"   using 1:6 with lines linewidth 2 linecolor rgb "black" title "static composition", \
     "dynamic_composition_rnd.csv"  using 1:6 with lines linewidth 2 linecolor rgb "blue" title "dynamic composition", \
     "dynamic_composition2_rnd.csv" using 1:6 with lines linewidth 2 linecolor rgb "green" title "dynamic composition v2"

set title "execution time (RND)"
set xlabel "number of tag machines"
set ylabel "time [ns]"
#set xrange [-30:40]
#set yrange [-30:40]
#set key font ",14"
plot "static_composition_rnd.csv"   using 1:5 with lines linewidth 2 linecolor rgb "black" title "static composition", \
     "dynamic_composition_rnd.csv"  using 1:5 with lines linewidth 2 linecolor rgb "blue" title "dynamic composition", \
     "dynamic_composition2_rnd.csv" using 1:5 with lines linewidth 2 linecolor rgb "green" title "dynamic composition v2"


set title "execution time (4 TM, RND)"
set xlabel "number of steps"
set ylabel "time [ns]"
#set xrange [-30:40]
#set yrange [-30:40]
#set key font ",14"
plot "static_composition_rnd_steps.csv"   using 4:5 with lines linewidth 2 linecolor rgb "black" title "static composition", \
     "dynamic_composition_rnd_steps.csv"  using 4:5 with lines linewidth 2 linecolor rgb "blue" title "dynamic composition", \
     "dynamic_composition2_rnd_steps.csv" using 4:5 with lines linewidth 2 linecolor rgb "green" title "dynamic composition v2"