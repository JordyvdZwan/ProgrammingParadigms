#include <kernels.h>
#include <stdio.h>
#include <omp.h>

void impl_matrix_multiply_openmp( float* A
                                , float* B
                                , float* C
                                , unsigned int Am
                                , unsigned int An
                                , unsigned int Bn
                                ) {

    // Implement this

}

void impl_matrix_countzero_openmp( float* M
                                 , unsigned int Mm
                                 , unsigned int Mn
                                 , unsigned int* Z
                                 ) {

    // Implement this

}

#define swap(a,b) do { \
  float tmp = a; \
  a = b; \
  b = tmp; \
} while(0)

void impl_vector_sort_openmp( float* V
                            , unsigned int Vn
                            ) {

    // Implement this

}

