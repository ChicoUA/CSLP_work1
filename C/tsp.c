//
// Francisco Jesus
// Student name
// ...
//
// AED, 2018/2019
//
// solution of the traveling salesman problem
//

#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/time.h>
#include <sys/resource.h>

#include "cities.h"
#include "../P01/elapsed_time.h"

//
// record best solutions
//

static int min_length,max_length;
static int min_tour[max_n_cities + 1],max_tour[max_n_cities + 1];
static long n_tours;
const int maxSize = 10000;
int *hist;


//
// first solution (brute force, distance computed at the end, compute best and worst tours)
//


void tsp_v1(int n,int m,int *a)
{
	int i, t, dis;
	int sumDis = 0;

  if(m < n - 1)
    for(i = m;i < n;i++)
    {
      t = a[m];
      a[m] = a[i];
      a[i] = t;
      tsp_v1(n,m + 1,a);
      t = a[m];
      a[m] = a[i];
      a[i] = t;
    }
  else
  { // visit permutation
    n_tours++;
    // modify the following code to do your stuff
	for (i = 0; i < n; i++) {

		
		if (i >= 1) {
			dis = cities[a[i - 1]].distance[a[i]];
			sumDis += dis;
		}
	}
	sumDis += cities[a[n - 1]].distance[0]; //para voltar ao inicio

	
	if (n == 12 || n == 15) {// array com valores do histograma
		hist[sumDis]++;

	}

	if (sumDis <= min_length) {
		min_length = sumDis;
		for (i = 0; i < n; i++) {
			min_tour[i] = a[i];
		}
	}
	if (sumDis >= max_length) {
		max_length = sumDis;
		for (i = 0; i < n; i++) {
			max_tour[i] = a[i];
		}
	}
  }
}


//
// main program
//

int main(int argc,char **argv)
{
  hist = malloc(sizeof(int*) * maxSize);
  if (!hist) { perror("malloc arr"); exit(EXIT_FAILURE); };


  int n_mec,special,n,i,a[max_n_cities];
  char file_name[32];
  double dt1;
  int n_cities = 15;
  n_mec = 89084; // CHANGE THIS!
  special = 1;
  init_cities_data(n_mec,special);
  printf("data for init_cities_data(%d,%d)\n",n_mec,special);
  fflush(stdout);
#if 0
  print_distances();
#endif
  for(n = 3;n <= n_cities;n++)
  {
    //
    // try tsp_v1
    //
	
    dt1 = -1.0;
    if(n <= 16)
    {
      (void)elapsed_time();
      for(i = 0;i < n;i++)
        a[i] = i;
      min_length = 1000000000;
      max_length = 0;
      n_tours = 0l;
      tsp_v1(n,1,a); // no need to change the starting city, as we are making a tour
      dt1 = elapsed_time();
	 
      printf("tsp_v1(%d) finished in %8.3fs (%ld tours generated)\n",n,dt1,n_tours);
      printf("  min %5d [",min_length);
      for(i = 0;i < n;i++)
        printf("%2d%s",min_tour[i],(i == n - 1) ? "]\n" : ",");
      printf("  max %5d [",max_length);
      for(i = 0;i < n;i++)
        printf("%2d%s",max_tour[i],(i == n - 1) ? "]\n" : ",");
	  
      fflush(stdout);
	  
      if(argc == 2 && strcmp(argv[1],"-f") == 0)
      {
        min_tour[n] = -1;
        sprintf(file_name,"min_%02d.svg",n);
        make_map(file_name,min_tour);
        max_tour[n] = -1;
        sprintf(file_name,"max_%02d.svg",n);
        make_map(file_name,max_tour);
      }
    }
  }
  /*
  for (i = 0; i < maxSize; i++) {
	 
	  printf(" %d%s", hist[i],(i==maxSize-1)? "\n" : " ");
  }
  */
  free(hist);

  return 0;
}