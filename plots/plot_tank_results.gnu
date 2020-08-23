set term postscript font 15 enhanced color

set output "tank_results.eps"
set title "tempo di esecuzione"
set xlabel "numero di step"
set ylabel "tempo [s]"
#set xrange [-30:40]
set yrange [0:3]
#set key font ",14"
plot "tank_results_static_composition.csv"   using 1:2 with lines linewidth 2 linecolor rgb "black" title "composizione statica", \
     "tank_results_dynamic_composition.csv"  using 1:2 with lines linewidth 2 linecolor rgb "blue" title "compisizone dinamica", \
     "tank_results_dynamic_composition2.csv" using 1:2 with lines linewidth 2 linecolor rgb "green" title "composizione dinamica con mappa hash"

set output "tank_results_rnd.eps"
set title "tempo di esecuzione"
set xlabel "numero di step"
set ylabel "tempo [s]"
#set xrange [-30:40]
set yrange [0:3]
#set key font ",14"
plot "tank_results_static_composition_rnd.csv"   using 1:2 with lines linewidth 2 linecolor rgb "black" title "composizione statica", \
     "tank_results_dynamic_composition_rnd.csv"  using 1:2 with lines linewidth 2 linecolor rgb "blue" title "compisizone dinamica", \
     "tank_results_dynamic_composition2_rnd.csv" using 1:2 with lines linewidth 2 linecolor rgb "green" title "composizione dinamica con mappa hash"
