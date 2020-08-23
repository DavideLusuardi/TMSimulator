set term postscript font 15 enhanced color
set output "results_p1.eps"

set title "utilizzo di memoria"
set xlabel "numbero di tag machine"
set ylabel "memoria [MB]"
#set xrange [-30:40]
#set yrange [-30:40]
#set key font ",14"
plot "static_composition.csv"   using 1:6 with lines linewidth 2 linecolor rgb "black" title "composizione statica", \
     "dynamic_composition.csv"  using 1:6 with lines linewidth 2 linecolor rgb "blue" title "composizione dinamica", \
     "dynamic_composition2.csv" using 1:6 with lines linewidth 2 linecolor rgb "green" title "composizione dinamica con mappa hash"

set output "results_p2.eps"
set title "tempo di esecuzione"
set xlabel "numero di tag machine"
set ylabel "tempo [s]"
#set xrange [-30:40]
#set yrange [-30:40]
#set key font ",14"
plot "static_composition.csv"   using 1:5 with lines linewidth 2 linecolor rgb "black" title "composizione statica", \
     "dynamic_composition.csv"  using 1:5 with lines linewidth 2 linecolor rgb "blue" title "composizione dinamica", \
     "dynamic_composition2.csv" using 1:5 with lines linewidth 2 linecolor rgb "green" title "composizione dinamica con mappa hash"


set output "results_p3.eps"
set title "tempo di esecuzione (4 TM)"
set xlabel "numbero di step"
set ylabel "tempo [s]"
#set xrange [-30:40]
set yrange [0:55]
#set key font ",14"
plot "static_composition_steps.csv"   using 4:5 with lines linewidth 2 linecolor rgb "black" title "composizione statica", \
     "dynamic_composition_steps.csv"  using 4:5 with lines linewidth 2 linecolor rgb "blue" title "composizione dinamica", \
     "dynamic_composition2_steps.csv" using 4:5 with lines linewidth 2 linecolor rgb "green" title "composizione dinamica con mappa hash"
set autoscale

set output "results_p4.eps"
set title "utilizzo di memoria (RND)"
set xlabel "numero di tag machine"
set ylabel "memoria [MB]"
#set xrange [-30:40]
#set yrange [-30:40]
#set key font ",14"
plot "static_composition_rnd.csv"   using 1:6 with lines linewidth 2 linecolor rgb "black" title "composizione statica", \
     "dynamic_composition_rnd.csv"  using 1:6 with lines linewidth 2 linecolor rgb "blue" title "composizione dinamica", \
     "dynamic_composition2_rnd.csv" using 1:6 with lines linewidth 2 linecolor rgb "green" title "composizione dinamica con mappa hash"


set output "results_p5.eps"
set title "tempo di esecuzione (RND)"
set xlabel "numero di tag machine"
set ylabel "tempo [s]"
#set xrange [-30:40]
#set yrange [-30:40]
#set key font ",14"
plot "static_composition_rnd.csv"   using 1:5 with lines linewidth 2 linecolor rgb "black" title "composizione statica", \
     "dynamic_composition_rnd.csv"  using 1:5 with lines linewidth 2 linecolor rgb "blue" title "composizione dinamica", \
     "dynamic_composition2_rnd.csv" using 1:5 with lines linewidth 2 linecolor rgb "green" title "composizione dinamica con mappa hash"


set output "results_p6.eps"
set title "tempo di esecuzione (4 TM, RND)"
set xlabel "numbero di step"
set ylabel "tempo [s]"
#set xrange [-30:40]
set yrange [0:55]
#set key font ",14"
plot "static_composition_rnd_steps.csv"   using 4:5 with lines linewidth 2 linecolor rgb "black" title "composizione statica", \
     "dynamic_composition_rnd_steps.csv"  using 4:5 with lines linewidth 2 linecolor rgb "blue" title "composizione dinamica", \
     "dynamic_composition2_rnd_steps.csv" using 4:5 with lines linewidth 2 linecolor rgb "green" title "composizione dinamica con mappa hash"
