set term postscript font 15 enhanced color
set output "results_steps_5tm.eps"

set title "tempo di esecuzione (5 TM)"
set xlabel "numero di step"
set ylabel "tempo [s]"
#set xrange [-30:40]
#set yrange [-30:40]
#set key font ",14"
plot "static_composition_steps_5tm.csv"   using 4:5 with lines linewidth 2 linecolor rgb "black" title "composizione statica", \
     "dynamic_composition_steps_5tm.csv"  using 4:5 with lines linewidth 2 linecolor rgb "blue" title "composizione dinamica", \
     "dynamic_composition2_steps_5tm.csv" using 4:5 with lines linewidth 2 linecolor rgb "green" title "composizione dinamica con mappa hash"

set output "results_steps_5tm_rnd.eps"
set title "tempo di esecuzione (5 TM, RND)"
set xlabel "numero di step"
set ylabel "tempo [s]"
#set xrange [-30:40]
#set yrange [-30:40]
#set key font ",14"
plot "static_composition_rnd_steps_5tm.csv"   using 4:5 with lines linewidth 2 linecolor rgb "black" title "composizione statica", \
     "dynamic_composition_rnd_steps_5tm.csv"  using 4:5 with lines linewidth 2 linecolor rgb "blue" title "composizione dinamica", \
     "dynamic_composition2_rnd_steps_5tm.csv" using 4:5 with lines linewidth 2 linecolor rgb "green" title "composizione dinamica con mappa hash"
